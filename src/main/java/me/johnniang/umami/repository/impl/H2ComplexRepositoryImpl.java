package me.johnniang.umami.repository.impl;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.umami.entity.Website;
import me.johnniang.umami.repository.ComplexRepository;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Map;

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
            DAY, "yyyy-MM-dd",
            MONTH, "yyyy-MM-01",
            YEAR, "yyyy-01-01"
    );

    private final EntityManager entityManager;

    public H2ComplexRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public WebsiteStats getWebsiteStats(Website website, LocalDateTime startAt, LocalDateTime endAt) {
        Object[] stats = (Object[]) getSession().createNativeQuery("\n" +
                "select sum(t.c) as pageViews,\n" +
                "   count(distinct t.sessionId) as uniques,\n" +
                "   sum(case when t.c = 1 then 1 else 0 end) as bounces,\n" +
                "   sum(t.time) as totalTime\n" +
                "from (\n" +
                "   select session_id as sessionId,\n" +
                "       formatdatetime(created_at, '" + DATE_FORMATS.get(HOUR) + "') as hours,\n" +
                "       count(*) as c,\n" +
                "       datediff('SECOND', min(created_at), max(created_at)) as time\n" +
                "   from pageview\n" +
                "   where website_id = :websiteId\n" +
                "   and created_at between :lowerTimeLimit and :upperTimeLimit\n" +
                "   group by sessionId, hours\n" +
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
    public Object getPageViewStats(Website website, LocalDateTime startAt, LocalDateTime endAt, String timezone, DateFormatUnit unit, String url) {
        return null;
    }
}
