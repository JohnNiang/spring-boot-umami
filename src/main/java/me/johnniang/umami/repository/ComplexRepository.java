package me.johnniang.umami.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.johnniang.umami.entity.Website;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
                          String countColumns,
                          String url);

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
        private LocalDateTime createdAt;

        @JsonProperty("y")
        private Long pageViews;

    }
}
