package me.johnniang.umami.security;

import javax.servlet.http.HttpServletRequest;

/**
 * Token parser.
 *
 * @author johnniang
 */
public interface TokenParser {

    String parse(HttpServletRequest request);

}
