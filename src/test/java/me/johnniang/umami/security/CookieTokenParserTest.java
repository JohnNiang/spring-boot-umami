package me.johnniang.umami.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cookie token parser test.
 *
 * @author johnniang
 */
class CookieTokenParserTest {

    @Test
    void parse() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        request.getCookies();
    }
}