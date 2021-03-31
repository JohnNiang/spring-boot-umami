package me.johnniang.umami.repository.impl;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.umami.entity.PageView;
import me.johnniang.umami.entity.Session;
import me.johnniang.umami.entity.Website;
import me.johnniang.umami.repository.ComplexRepository;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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
    public List<Metric> getSessionMetrics(Website website, LocalDateTime startAt, LocalDateTime endAt, String field, Map<String, Object> filter) {
        final var cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> query = cb.createQuery();
        Root<Session> root = query.from(Session.class);
        Path<Object> fieldPath = root.get(field);
        Expression<Long> countPath = cb.count(cb.literal("*"));
        query.multiselect(fieldPath, countPath);

        // sub query
        Subquery<Session> subQuery = query.subquery(Session.class);
        Root<PageView> subRoot = subQuery.from(PageView.class);
        subQuery.select(subRoot.get("session"))
                .where(
                        cb.equal(subRoot.get("website"), website),
                        cb.between(subRoot.get("createdAt"), startAt, endAt)
                );
        // sub query end

        query.where(root.get("id").in(subQuery))
                .groupBy(fieldPath)
                .orderBy(cb.desc(countPath));

        List<Object> rawMetrics = entityManager.createQuery(query).getResultList();
        return rawMetrics.stream().map(rawMetric -> {
            Object[] rawMetricArr = (Object[]) rawMetric;
            Metric metric = new Metric();
            if (rawMetricArr[0] != null) {
                metric.setX(rawMetricArr[0].toString());
            }
            metric.setY(((Number) rawMetricArr[1]).longValue());
            return metric;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EventMetric> getEventMetrics(Website website, LocalDateTime startAt, LocalDateTime endAt, TimeZone timeZone, DateFormatUnit unit, Object filter) {
        return delegate.getEventMetrics(website, startAt, endAt, timeZone, unit, filter);
    }

    @Override
    public List<Metric> getMetrics(Website website, LocalDateTime startAt, LocalDateTime endAt, Class<?> entityClass, String field, QueryFilter filter) {
        final var cb = entityManager.getCriteriaBuilder();
        final var query = cb.createQuery();
        Root<?> root = query.from(entityClass);
        Expression<Long> count = cb.count(cb.literal("*"));
        Path<Object> fieldPath = root.get(field);
        query.multiselect(fieldPath, count);
        query.groupBy(fieldPath);
        query.orderBy(cb.desc(count));

        Predicate filterPredicate = cb.isTrue(cb.literal(true));
        if (filter != null) {
            filterPredicate = cb.and(filterPredicate, filter.build(root, cb));
        }
        query.where(
                cb.equal(root.get("website"), website),
                cb.between(root.get("createdAt"), startAt, endAt),
                filterPredicate
        );

        List<Object> rawMetrics = entityManager.createQuery(query).getResultList();
        return rawMetrics.stream().map(rawMetric -> {
            Object[] rawMetricArr = (Object[]) rawMetric;
            Metric metric = new Metric();
            if (rawMetricArr[0] != null) {
                metric.setX(rawMetricArr[0].toString());
            }
            metric.setY(((Number) rawMetricArr[1]).longValue());
            return metric;
        }).collect(Collectors.toList());
    }

    @Override
    public Long getActiveVisitors(Website website) {
        final var cb = entityManager.getCriteriaBuilder();
        final var query = cb.createQuery();
        Root<PageView> root = query.from(PageView.class);
        query.select(cb.countDistinct(root.get("session")));
        query.where(
                cb.equal(root.get("website"), website),
                cb.greaterThanOrEqualTo(root.get("createdAt"), LocalDateTime.now().minusMinutes(5))
        );
        Object rawActiveVisitors = entityManager.createQuery(query).getSingleResult();
        return ((Number) rawActiveVisitors).longValue();
    }

}
