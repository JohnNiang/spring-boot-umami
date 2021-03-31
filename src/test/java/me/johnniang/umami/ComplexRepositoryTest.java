package me.johnniang.umami;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.umami.entity.PageView;
import me.johnniang.umami.entity.Website;
import me.johnniang.umami.repository.ComplexRepository;
import me.johnniang.umami.repository.ComplexRepository.Metric;
import me.johnniang.umami.repository.impl.ComplexRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.List;

import static me.johnniang.umami.repository.ComplexRepository.DateFormatUnit.DAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void criteriaBuilderTest() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    }

    @Test
    void buildQueryFilterTest() {
        ComplexRepository.QueryFilter queryFilter = (root, cb) -> {
            return cb.like(root.get("referrer"), "");
        };
    }
}
