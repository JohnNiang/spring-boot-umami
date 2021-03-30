package me.johnniang.umami.repository.impl;

import me.johnniang.umami.entity.Website;
import me.johnniang.umami.repository.ComplexRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static me.johnniang.umami.repository.ComplexRepository.DateFormatUnit.*;

public class MySQLComplexRepositoryImpl implements ComplexRepository {

    private static final Map<DateFormatUnit, String> DATE_FORMATS = Map.of(
            MINUTE, "%Y-%m-%d %H:%i:00",
            HOUR, "%Y-%m-%d %H:00:00",
            DAY, "%Y-%m-%d",
            MONTH, "%Y-%m-01",
            YEAR, "%Y-01-01"
    );

    private final EntityManager entityManager;

    public MySQLComplexRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public WebsiteStats getWebsiteStats(Website website, LocalDateTime startAt, LocalDateTime endAt) {

        return null;
    }

    @Override
    public List<PageViewStats> getPageViewStats(Website website, LocalDateTime startAt, LocalDateTime endAt, String timezone, DateFormatUnit unit, String countColumns, String url) {
        return null;
    }

}