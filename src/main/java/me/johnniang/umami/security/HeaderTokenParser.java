package me.johnniang.umami.security;

import me.johnniang.umami.UmamiConst;

import javax.servlet.http.HttpServletRequest;

/**
 * Parser for token from header.
 *
 * @author johnniang
 */
public class HeaderTokenParser implements TokenParser {

    @Override
    public String parse(HttpServletRequest request) {
        return request.getHeader(UmamiConst.TOKEN_HEADER);
    }

}
