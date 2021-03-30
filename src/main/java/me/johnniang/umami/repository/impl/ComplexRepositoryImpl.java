package me.johnniang.umami.repository.impl;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.umami.entity.Website;
import me.johnniang.umami.repository.ComplexRepository;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
public class ComplexRepositoryImpl implements ComplexRepository {

    private final ComplexRepository delegate;

    public ComplexRepositoryImpl(EntityManager entityManager) {
        // deduct which database could be loaded
        Object dialect = entityManager.getEntityManagerFactory().getProperties().get(AvailableSettings.DIALECT);
        if (dialect == null || "org.hibernate.dialect.H2Dialect".equals(dialect)) {
            // h2
            delegate = new H2ComplexRepositoryImpl(entityManager);
        } else if ("org.hibernate.dialect.MySQL8Dialect".equals(dialect)) {
            delegate = new MySQLComplexRepositoryImpl(entityManager);
        } else {
            throw new IllegalArgumentException("Illegal dialect: " + dialect + " for complex repository implementation.");
        }
    }

    @Override
    public WebsiteStats getWebsiteStats(Website website, LocalDateTime startAt, LocalDateTime endAt) {
        return delegate.getWebsiteStats(website, startAt, endAt);
    }

    @Override
    public List<PageViewStats> getPageViewStats(Website website, LocalDateTime startAt, LocalDateTime endAt, String timezone, DateFormatUnit unit, String countColumns, String url) {
        return delegate.getPageViewStats(website, startAt, endAt, timezone, unit, countColumns, url);
    }

}
