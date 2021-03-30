package me.johnniang.umami.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.johnniang.umami.entity.Website;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Complex repository.
 *
 * @author johnniang
 */
public interface ComplexRepository {

    WebsiteStats getWebsiteStats(Website website, LocalDateTime startAt, LocalDateTime endAt);

    List<PageViewStats> getPageViewStats(Website website,
                                         LocalDateTime startAt,
                                         LocalDateTime endAt,
                                         String timezone,
                                         DateFormatUnit unit,
                                         String countFields,
                                         String url);

    default List<Metrics> getSessionMetrics(Website website, LocalDateTime startAt, LocalDateTime endAt, String field, Map<String, Object> filter) {
        throw new UnsupportedOperationException("Not implement");
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
    class Metrics {

        private String x;

        private Long y;

        public static Metrics of(String x, Long y) {
            Metrics metrics = new Metrics();
            metrics.setX(x);
            metrics.setY(y);
            return metrics;
        }
    }
}
