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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Spring data jpa loading test.
 *
 * @author johnniang
 */
@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Import({ComplexRepositoryImpl.class, UmamiConfiguration.class})
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
        ComplexRepository.WebsiteStats websiteStats = complexRepository.getWebsiteStats(website, LocalDateTime.now().minusDays(2), LocalDateTime.now());
        Assertions.assertEquals(28, websiteStats.getPageViews());
        Assertions.assertEquals(2, websiteStats.getUniques());
        Assertions.assertEquals(1, websiteStats.getBounces());
        Assertions.assertEquals(347, websiteStats.getTotalTime());
    }

}
