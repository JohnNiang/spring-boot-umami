package me.johnniang.umami;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.umami.entity.Event;
import me.johnniang.umami.entity.PageView;
import me.johnniang.umami.entity.Session;
import me.johnniang.umami.entity.Website;
import me.johnniang.umami.repository.ComplexRepository;
import me.johnniang.umami.repository.ComplexRepository.EventMetric;
import me.johnniang.umami.repository.ComplexRepository.Metric;
import me.johnniang.umami.repository.EventRepository;
import me.johnniang.umami.repository.PageViewRepository;
import me.johnniang.umami.repository.SessionRepository;
import me.johnniang.umami.repository.impl.ComplexRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static me.johnniang.umami.repository.ComplexRepository.DateFormatUnit.DAY;
import static me.johnniang.umami.repository.ComplexRepository.DateFormatUnit.HOUR;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring data jpa loading test.
 *
 * @author johnniang
 */
@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Import({ComplexRepositoryImpl.class})
class ComplexRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ComplexRepository complexRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    PageViewRepository pageViewRepository;

    @Autowired
    EventRepository eventRepository;

    @Test
    void contextLoad() {
        assertNotNull(complexRepository);
    }

    @Test
    void getWebsiteStatsQuery() {
        Website website = new Website();
        website.setId(1);
        LocalDateTime startAt = LocalDateTime.of(2021, 3, 28, 0, 0, 0);
        LocalDateTime endAt = startAt.plusDays(2);
        ComplexRepository.WebsiteStats websiteStats = complexRepository.getWebsiteStats(website, startAt, endAt);
        assertEquals(28, websiteStats.getPageViews());
        assertEquals(2, websiteStats.getUniques());
        assertEquals(1, websiteStats.getBounces());
        assertEquals(347, websiteStats.getTotalTime());
    }

    @Test
    void getPageViewStatsQuery() {
        Website website = new Website();
        website.setId(1);
        LocalDateTime startAt = LocalDateTime.of(2021, 3, 28, 0, 0, 0);
        LocalDateTime endAt = startAt.plusDays(3);
        List<ComplexRepository.PageViewStats> pageViewStats = complexRepository.getPageViewStats(website, startAt, endAt, "Asia/Shanghai", DAY, null, null);

        assertEquals(1, pageViewStats.size());
        assertEquals(LocalDateTime.of(2021, 3, 29, 0, 0, 0), pageViewStats.get(0).getDateTime());
        assertEquals(28, pageViewStats.get(0).getPageViews());
    }

    @Test
    void getSessionMetricsQuery() {
        Website website = new Website();
        website.setId(1);
        LocalDateTime startAt = LocalDateTime.of(2021, 3, 28, 0, 0, 0);
        LocalDateTime endAt = startAt.plusDays(3);

        List<Metric> metrics = complexRepository.getSessionMetrics(website, startAt, endAt, "os", null);
        assertEquals(List.of(Metric.of("Linux", 2L)), metrics);

        metrics = complexRepository.getSessionMetrics(website, startAt, endAt, "device", null);
        assertEquals(List.of(Metric.of("desktop", 2L)), metrics);

        metrics = complexRepository.getSessionMetrics(website, startAt, endAt, "country", null);
        assertEquals(List.of(Metric.of(null, 2L)), metrics);

        metrics = complexRepository.getSessionMetrics(website, startAt, endAt, "browser", null);
        assertEquals(List.of(Metric.of("chrome", 1L), Metric.of("firefox", 1L)), metrics);
    }

    @Test
    void getReferrerMetricsWithoutFilter() {
        Website website = new Website();
        website.setId(1);
        LocalDateTime startAt = LocalDateTime.of(2021, 3, 28, 0, 0, 0);
        LocalDateTime endAt = startAt.plusDays(3);

        List<Metric> metrics = complexRepository.getMetrics(website, startAt, endAt, PageView.class, "referrer", null);
        List<Metric> expectedMetrics = List.of(
                Metric.of("http://localhost:8090/", 5L),
                Metric.of("http://localhost:8090/archives/hello-halo", 5L),
                Metric.of("http://localhost:8090/categories/default", 5L),
                Metric.of("http://localhost:8090/s/about", 5L),
                Metric.of("http://localhost:8090/archives", 3L),
                Metric.of("", 2L),
                Metric.of("http://localhost:8090/admin/index.html", 1L),
                Metric.of("http://localhost:8090/search?keyword=a", 1L),
                Metric.of("http://localhost:8090/search?keyword=b", 1L)
        );
        assertEquals(expectedMetrics, metrics);
    }

    @Test
    void getReferrerMetricsWithFilter() {
        Website website = new Website();
        website.setId(1);
        LocalDateTime startAt = LocalDateTime.of(2021, 3, 28, 0, 0, 0);
        LocalDateTime endAt = startAt.plusDays(3);

        List<Metric> metrics = complexRepository.getMetrics(website, startAt, endAt, PageView.class, "referrer", (root, cb) -> {
            Predicate httpLike = cb.like(root.get("referrer"), "http://localhost:8090/search%");
            Predicate httpsLike = cb.like(root.get("referrer"), "https://localhost:8090/search%");
            return cb.or(httpLike, httpsLike);
        });

        List<Metric> expectedMetrics = List.of(
                Metric.of("http://localhost:8090/search?keyword=a", 1L),
                Metric.of("http://localhost:8090/search?keyword=b", 1L)
        );

        assertEquals(expectedMetrics, metrics);
    }

    @Test
    void getUrlMetricsQueryWithoutFilter() {
        Website website = new Website();
        website.setId(1);
        LocalDateTime startAt = LocalDateTime.of(2021, 3, 28, 0, 0, 0);
        LocalDateTime endAt = startAt.plusDays(3);

        List<Metric> metrics = complexRepository.getMetrics(website, startAt, endAt, PageView.class, "url", null);
        List<Metric> expectedMetrics = List.of(Metric.of("/", 9L),
                Metric.of("/categories/default", 5L),
                Metric.of("/s/about", 5L),
                Metric.of("/archives/hello-halo", 4L),
                Metric.of("/archives", 3L),
                Metric.of("/search?keyword=a", 1L),
                Metric.of("/search?keyword=b", 1L));
        assertEquals(expectedMetrics, metrics);
    }

    @Test
    void getUrlMetricsQueryWithFilter() {
        Website website = new Website();
        website.setId(1);
        LocalDateTime startAt = LocalDateTime.of(2021, 3, 28, 0, 0, 0);
        LocalDateTime endAt = startAt.plusDays(3);

        List<Metric> metrics = complexRepository.getMetrics(website, startAt, endAt, PageView.class, "url", (root, cb) -> cb.like(root.get("url"), "/archives%"));
        List<Metric> expectedMetrics = List.of(
                Metric.of("/archives/hello-halo", 4L),
                Metric.of("/archives", 3L)
        );
        assertEquals(expectedMetrics, metrics);
    }

    @Test
    void getActiveVisitorsWithoutVisiting() {
        Website website = new Website();
        website.setId(1);

        Long activeVisitors = complexRepository.getActiveVisitors(website);
        assertEquals(0L, activeVisitors);
    }

    @Test
    void getActiveVisitorsWithVisiting() {
        final var website = new Website();
        website.setId(1);
        // simulate visiting
        List<Session> sessions = IntStream.range(0, 3).mapToObj(i -> {
            Session session = new Session();
            session.setUuid(UUID.randomUUID().toString());
            session.setWebsite(website);
            return session;
        }).collect(Collectors.toList());
        sessionRepository.saveAll(sessions);

        List<PageView> pageViews = sessions.stream().flatMap(session -> IntStream.range(0, 5).mapToObj(i -> {
            PageView pageView = new PageView();
            pageView.setWebsite(website);
            pageView.setSession(session);
            pageView.setUrl("/test");
            return pageView;
        })).collect(Collectors.toList());
        pageViewRepository.saveAll(pageViews);
        // simulate visiting end

        // get active visitors
        Long activeVisitors = complexRepository.getActiveVisitors(website);
        assertEquals(sessions.size(), activeVisitors);
    }

    @Test
    void getEventMetricsWithoutTrigger() {
        Website website = new Website();
        website.setId(1);
        LocalDateTime startAt = LocalDateTime.of(2021, 3, 28, 0, 0, 0);
        LocalDateTime endAt = startAt.plusDays(2);

        List<EventMetric> eventMetrics = complexRepository.getEventMetrics(website, startAt, endAt, TimeZone.getTimeZone("UTC"), HOUR, null);
        assertTrue(eventMetrics.isEmpty());
    }

    @Test
    void getEventMetricsWithTrigger() {
        Website website = new Website();
        website.setId(1);

        // simulate visiting
        List<Session> sessions = IntStream.range(0, 3).mapToObj(i -> {
            Session session = new Session();
            session.setUuid(UUID.randomUUID().toString());
            session.setWebsite(website);
            return session;
        }).collect(Collectors.toList());
        sessionRepository.saveAll(sessions);

        List<Event> events = sessions.stream().flatMap(session -> IntStream.range(0, 5).mapToObj(i -> {
            Event event = new Event();
            event.setWebsite(website);
            event.setSession(session);
            event.setUrl("test-url");
            event.setEventType("test-event-type");
            event.setEventValue("test-event-value");
            return event;
        })).collect(Collectors.toList());
        eventRepository.saveAll(events);

        // simulate visiting end

        LocalDateTime endAt = events.get(events.size() - 1).getCreatedAt();
        LocalDateTime startAt = endAt.minusMinutes(5);
        List<EventMetric> eventMetrics = complexRepository.getEventMetrics(website, startAt, endAt, TimeZone.getTimeZone("CST"), DAY, null);
        EventMetric expectEventMetric = new EventMetric();
        expectEventMetric.setX("test-event-value");
        expectEventMetric.setY((long) events.size());
        expectEventMetric.setT(LocalDateTime.of(endAt.toLocalDate(), LocalTime.MIN));
        assertEquals(List.of(expectEventMetric), eventMetrics);
    }

}
