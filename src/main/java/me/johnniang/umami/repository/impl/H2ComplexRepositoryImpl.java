package me.johnniang.umami.repository.impl;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.umami.entity.Website;
import me.johnniang.umami.repository.ComplexRepository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static me.johnniang.umami.repository.ComplexRepository.DateFormatUnit.*;

/**
 * H2 complex repository implementation.
 *
 * @author johnniang
 */
@Slf4j
public class H2ComplexRepositoryImpl implements ComplexRepository {

    private static final Map<DateFormatUnit, String> DATE_FORMATS = Map.of(
            MINUTE, "yyyy-MM-dd HH:mm:00",
            HOUR, "yyyy-MM-dd HH:00:00",
            DAY, "yyyy-MM-dd 00:00:00",
            MONTH, "yyyy-MM-01 00:00:00",
            YEAR, "yyyy-01-01 00:00:00"
    );

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EntityManager entityManager;

    public H2ComplexRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public WebsiteStats getWebsiteStats(Website website, LocalDateTime startAt, LocalDateTime endAt) {
        Object[] stats = (Object[]) entityManager.createNativeQuery("\n" +
                "select sum(t.c) as pageViews,\n" +
                "   count(distinct t.sessionId) as uniques,\n" +
                "   sum(case when t.c = 1 then 1 else 0 end) as bounces,\n" +
                "   sum(t.time) as totalTime\n" +
                "from (\n" +
                "   select session_id as sessionId,\n" +
                "       " + getDateQuery("created_at", HOUR, null) + " as hours,\n" +
                "       count(*) as c,\n" +
                "       " + getTimestampIntervalQuery("created_at") + " as time\n" +
                "   from pageview\n" +
                "   where website_id = :websiteId\n" +
                "       and created_at between :lowerTimeLimit and :upperTimeLimit\n" +
                "       group by sessionId, hours\n" +
                ") as t" +
                "\n")
                .setParameter("websiteId", website.getId())
                .setParameter("lowerTimeLimit", startAt)
                .setParameter("upperTimeLimit", endAt)
                .getSingleResult();

        WebsiteStats websiteStats = new WebsiteStats();
        websiteStats.setPageViews(((Number) stats[0]).longValue());
        websiteStats.setUniques(((Number) stats[1]).intValue());
        websiteStats.setBounces(((Number) stats[2]).intValue());
        websiteStats.setTotalTime(((Number) stats[3]).longValue());
        return websiteStats;
    }

    @Override
    public List<PageViewStats> getPageViewStats(Website website, LocalDateTime startAt, LocalDateTime endAt, String timezone, DateFormatUnit unit, String countFields, String url) {
        TimeZone tz = TimeZone.getTimeZone(timezone);
        if (!StringUtils.hasText(countFields)) {
            countFields = "*";
        }
        StringBuilder nativeSql = new StringBuilder("select ").append(getDateQuery("created_at", unit, tz)).append(" as t,\n")
                .append("   count(").append(countFields).append(") as y\n")
                .append("from pageview\n")
                .append("where website_id = :websiteId\n")
                .append("   and created_at between :lowerTimeLimit and :upperTimeLimit");
        if (StringUtils.hasText(url)) {
            nativeSql.append(" and url='").append(url).append('\'');
        }
        nativeSql.append('\n');
        nativeSql.append("group by t\n")
                .append("order by t\n").append("\n");
        List<?> stats = entityManager.createNativeQuery(nativeSql.toString())
                .setParameter("websiteId", website.getId())
                .setParameter("lowerTimeLimit", startAt)
                .setParameter("upperTimeLimit", endAt)
                .getResultList();

        return stats.stream().map(stat -> {
            Object[] statArr = (Object[]) stat;
            PageViewStats pageViewStats = new PageViewStats();
            pageViewStats.setDateTime(LocalDateTime.parse(statArr[0].toString(), DATE_TIME_FORMATTER));
            pageViewStats.setPageViews(((Number) statArr[1]).longValue());
            return pageViewStats;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EventMetric> getEventMetrics(Website website, LocalDateTime startAt, LocalDateTime endAt, TimeZone timeZone, DateFormatUnit unit, Object filter) {
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class);
//        Root<Event> root = query.from(Event.class);
//        Path<Object> eventValuePath = root.get("eventValue");
//        Expression<LocalDateTime> formattedCreatedAtPath = cb.function("formatdatetime", LocalDateTime.class, root.get("createdAt"), cb.literal(DATE_FORMATS.get(unit)), cb.literal("en"), cb.literal(timeZone.getID()));
//        Expression<Long> countPath = cb.count(cb.literal("*"));
//        query.multiselect(eventValuePath.alias("x"), formattedCreatedAtPath.alias("t"), countPath.alias("y"));
//        query.where(
//                cb.equal(root.get("website"), website),
//                cb.between(root.get("createdAt"), startAt, endAt)
//        );
//
//        query.getSelection();
//
//        query.groupBy(eventValuePath, formattedCreatedAtPath)
//                .orderBy(cb.asc(formattedCreatedAtPath));
        // handle for filter

        final var nativeSql = new StringBuilder("select event_value x,").append('\n')
                .append("   ").append(getDateQuery("created_at", unit, timeZone)).append(" as t,").append('\n')
                .append("   count(*) y").append('\n')
                .append("from event").append('\n')
                .append("where website_id = :websiteId").append('\n')
                .append("   and created_at between :lowerTimeLimit and :upperTimeLimit").append('\n')
                .append("group by x, t").append('\n')
                .append("order by t");

        List<?> rawMetrics = entityManager.createNativeQuery(nativeSql.toString())
                .setParameter("websiteId", website.getId())
                .setParameter("lowerTimeLimit", startAt)
                .setParameter("upperTimeLimit", endAt)
                .getResultList();

        return rawMetrics.stream().map(rawMetric -> {
            Object[] metric = (Object[]) rawMetric;
            EventMetric eventMetric = new EventMetric();
            if (metric[0] != null) {
                eventMetric.setX(metric[0].toString());
            }
            if (metric[1] != null) {
                eventMetric.setT(LocalDateTime.parse(metric[1].toString(), DATE_TIME_FORMATTER));
            }
            if (metric[2] != null) {
                eventMetric.setY(((Number) metric[2]).longValue());
            }
            return eventMetric;
        }).collect(Collectors.toList());
    }

    protected String getDateQuery(String field, DateFormatUnit unit, TimeZone timeZone) {
        StringBuilder sb = new StringBuilder("formatdatetime");
        sb.append('(').append(field);
        sb.append(',').append('\'').append(DATE_FORMATS.get(unit)).append('\'');
        if (timeZone != null) {
            sb.append(',').append('\'').append("en").append('\'');
            sb.append(',').append('\'').append(timeZone.getID()).append('\'');
        }
        sb.append(')');
        return sb.toString();
    }

    protected String getTimestampIntervalQuery(String field) {
        return "datediff('SECOND', min(" + field + "),max(" + field + "))";
    }
}
