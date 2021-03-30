package me.johnniang.umami.repository.impl;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.umami.entity.Website;
import me.johnniang.umami.repository.ComplexRepository;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ComplexRepositoryImpl implements ComplexRepository {

    private final EntityManager entityManager;

    private final ComplexRepository delegate;

    public ComplexRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
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
    public List<PageViewStats> getPageViewStats(Website website, LocalDateTime startAt, LocalDateTime endAt, String timezone, DateFormatUnit unit, String countFields, String url) {
        return delegate.getPageViewStats(website, startAt, endAt, timezone, unit, countFields, url);
    }

    @Override
    public List<Metrics> getSessionMetrics(Website website, LocalDateTime startAt, LocalDateTime endAt, String field, Map<String, Object> filter) {
        return delegate.getSessionMetrics(website, startAt, endAt, field, filter);
    }

    @Override
    public List<Metrics> getPageMetrics(Website website, LocalDateTime startAt, LocalDateTime endAt, Class<?> entityClass, String field, Map<String, Object> filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery();
        Root<?> root = query.from(entityClass);
        Expression<Long> count = cb.count(cb.literal("*"));
        Path<Object> fieldPath = root.get(field);
        query.multiselect(fieldPath, count);
        query.groupBy(fieldPath);
        query.orderBy(cb.desc(count));

        query.where(
                cb.equal(root.get("website"), website),
                cb.between(root.get("createdAt"), startAt, endAt)
        );

        List<Object> rawMetrics = entityManager.createQuery(query).getResultList();
        return rawMetrics.stream().map(rawMetric -> {
            Object[] rawMetricArr = (Object[]) rawMetric;
            Metrics metrics = new Metrics();
            if (rawMetricArr[0] != null) {
                metrics.setX(rawMetricArr[0].toString());
            }
            metrics.setY(((Number) rawMetricArr[1]).longValue());
            return metrics;
        }).collect(Collectors.toList());
    }

}
