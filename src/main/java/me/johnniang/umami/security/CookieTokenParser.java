package me.johnniang.umami.security;

import me.johnniang.umami.UmamiConst;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Parser for token from cookie.
 *
 * @author johnniang
 */
public class CookieTokenParser implements TokenParser {

    @Override
    public String parse(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (UmamiConst.AUTH_COOKIE_NAME.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }
}
