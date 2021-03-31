package me.johnniang.umami.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.johnniang.umami.entity.Website;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Complex repository.
 *
 * @author johnniang
 */
public interface ComplexRepository {

    default WebsiteStats getWebsiteStats(Website website, LocalDateTime startAt, LocalDateTime endAt) {
        throw new UnsupportedOperationException("Not implement");
    }

    default List<PageViewStats> getPageViewStats(Website website,
                                                 LocalDateTime startAt,
                                                 LocalDateTime endAt,
                                                 String timezone,
                                                 DateFormatUnit unit,
                                                 String countFields,
                                                 String url) {
        throw new UnsupportedOperationException("Not implement");
    }

    default List<Metric> getSessionMetrics(Website website, LocalDateTime startAt, LocalDateTime endAt, String field, Map<String, Object> filter) {
        throw new UnsupportedOperationException("Not implement");
    }

    default List<Metric> getMetrics(Website website, LocalDateTime startAt, LocalDateTime endAt, Class<?> entityClass, String field, QueryFilter filter) {
        throw new UnsupportedOperationException("Not implement");
    }

    default Long getActiveVisitors(Website website) {
        throw new UnsupportedOperationException("Not implement");
    }

    /**
     * Query filter.
     *
     * @author johnniang
     */
    interface QueryFilter {

        Predicate build(Root<?> root, CriteriaBuilder cb);
    }

    /**
     * Date format unit.
     *
     * @author johnniang
     */
    enum DateFormatUnit {

        MINUTE,

        HOUR,

        DAY,

        MONTH,

        YEAR
    }

    /**
     * Website stats model.
     *
     * @author johnniang
     */
    @Data
    class WebsiteStats {

        @JsonProperty("pageviews")
        private Long pageViews;

        private Integer uniques;

        private Integer bounces;

        @JsonProperty("totaltime")
        private Long totalTime;
    }

    /**
     * PageView stats model.
     *
     * @author johnniang
     */
    @Data
    class PageViewStats {

        @JsonProperty("t")
        private LocalDateTime dateTime;

        @JsonProperty("y")
        private Long pageViews;

    }

    /**
     * Metrics view.
     *
     * @author johnniang
     */
    @Data
    class Metric {

        private String x;

        private Long y;

        public static Metric of(String x, Long y) {
            Metric metric = new Metric();
            metric.setX(x);
            metric.setY(y);
            return metric;
        }
    }
}
