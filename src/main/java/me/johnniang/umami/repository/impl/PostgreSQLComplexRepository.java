package me.johnniang.umami.repository.impl;

import me.johnniang.umami.entity.Website;
import me.johnniang.umami.repository.ComplexRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static me.johnniang.umami.repository.ComplexRepository.DateFormatUnit.*;

/**
 * Postgre SQL complex repository.
 *
 * @author johnniang
 */
public class PostgreSQLComplexRepository implements ComplexRepository {

    private static final Map<DateFormatUnit, String> DATE_FORMATS = Map.of(
            MINUTE, "YYYY-MM-DD HH24:MI:00",
            HOUR, "YYYY-MM-DD HH24:00:00",
            DAY, "YYYY-MM-DD",
            MONTH, "YYYY-MM-01",
            YEAR, "YYYY-01-01"
    );

    @Override
    public WebsiteStats getWebsiteStats(Website website, LocalDateTime startAt, LocalDateTime endAt) {
        return null;
    }

    @Override
    public List<PageViewStats> getPageViewStats(Website website, LocalDateTime startAt, LocalDateTime endAt, String timezone, DateFormatUnit unit, String countFields, String url) {
        return null;
    }
}
