package me.johnniang.umami;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.umami.entity.Website;
import me.johnniang.umami.repository.ComplexRepository;
import me.johnniang.umami.repository.ComplexRepository.Metrics;
import me.johnniang.umami.repository.impl.ComplexRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

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
        List<ComplexRepository.PageViewStats> pageViewStats = complexRepository.getPageViewStats(website, startAt, endAt, "Asia/Shanghai", ComplexRepository.DateFormatUnit.DAY, null, null);

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

        List<Metrics> metrics = complexRepository.getSessionMetrics(website, startAt, endAt, "os", null);
        assertEquals(List.of(Metrics.of("Linux", 2L)), metrics);

        metrics = complexRepository.getSessionMetrics(website, startAt, endAt, "device", null);
        assertEquals(List.of(Metrics.of("desktop", 2L)), metrics);

        metrics = complexRepository.getSessionMetrics(website, startAt, endAt, "country", null);
        assertEquals(List.of(Metrics.of(null, 2L)), metrics);

        metrics = complexRepository.getSessionMetrics(website, startAt, endAt, "browser", null);
        assertEquals(List.of(Metrics.of("chrome", 1L), Metrics.of("firefox", 1L)), metrics);

//        metrics = complexRepository.getSessionMetrics(website, startAt, endAt, "referrer", null);
//        assertEquals(List.of(
//                Metrics.of("http://localhost:8090/", 5L),
//                Metrics.of("http://localhost:8090/archives/hello-halo", 5L),
//                Metrics.of("http://localhost:8090/categories/default", 5L),
//                Metrics.of("http://localhost:8090/s/about", 5L),
//                Metrics.of("http://localhost:8090/archives", 3L),
//                Metrics.of("", 2L),
//                Metrics.of("http://localhost:8090/admin/index.html", 1L),
//                Metrics.of("http://localhost:8090/search?keyword=a", 1L),
//                Metrics.of("http://localhost:8090/search?keyword=b", 1L)
//        ), metrics);
    }
}
