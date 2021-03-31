package me.johnniang.umami.security;

import me.johnniang.umami.cache.Cache;
import me.johnniang.umami.entity.Account;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Security filter.
 *
 * @author johnniang
 */
public class SecurityFilter extends OncePerRequestFilter {

    private final Cache cache;

    public SecurityFilter(Cache cache) {
        this.cache = cache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // get token from cookie
        var tokenParser = new CookieTokenParser();
        String token = tokenParser.parse(request);
        // get token from header
        if (token == null) {
            // token not present
            // throw invalid credential exception
            throw new IllegalArgumentException("Token required");
        }

        Account account = cache.get(token, Account.class).orElseThrow(() -> new IllegalArgumentException("Invalid token or expired token"));

        // concrete context
        DefaultRequestContext context = new DefaultRequestContext(token, account);
        // prepare context
        // TODO resolve other context
        // refresh context

        try {
            SecurityContextHolder.setContext(context);
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.resetContext();
        }
    }
}
