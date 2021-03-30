package me.johnniang.umami;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.umami.config.UmamiConfiguration;
import me.johnniang.umami.entity.Website;
import me.johnniang.umami.repository.AccountRepository;
import me.johnniang.umami.repository.ComplexRepository;
import me.johnniang.umami.repository.impl.ComplexRepositoryImpl;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
class UmamiDataJpaTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    Session session;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ComplexRepository complexRepository;

    @Test
    void contextLoad() {
        assertNotNull(accountRepository);
        assertNotNull(complexRepository);
        assertNotNull(session);
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
        assertEquals(LocalDate.of(2021, 3, 29), pageViewStats.get(0).getCreatedAt());
        assertEquals(28, pageViewStats.get(0).getPageViews());
    }

}
