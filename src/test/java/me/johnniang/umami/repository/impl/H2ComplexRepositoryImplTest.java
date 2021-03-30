package me.johnniang.umami.repository.impl;

import me.johnniang.umami.repository.ComplexRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * H2 complex repository implementation test.
 *
 * @author johnniang
 */
class H2ComplexRepositoryImplTest {

    H2ComplexRepositoryImpl h2ComplexRepository;

    @BeforeEach
    void setUp() {
        h2ComplexRepository = new H2ComplexRepositoryImpl(null);
    }

    @Test
    void getDateQueryWithTimezone() {
        String dateQuery = h2ComplexRepository.getDateQuery("createAt", ComplexRepository.DateFormatUnit.HOUR, TimeZone.getTimeZone("UTC"));
        assertEquals("formatdatetime(createAt,'yyyy-MM-dd HH:00:00','en','UTC')", dateQuery);
    }

    @Test
    void getDateQueryWithoutTimezone() {
        String dateQuery = h2ComplexRepository.getDateQuery("createAt", ComplexRepository.DateFormatUnit.HOUR, null);
        assertEquals("formatdatetime(createAt,'yyyy-MM-dd HH:00:00')", dateQuery);
    }
}