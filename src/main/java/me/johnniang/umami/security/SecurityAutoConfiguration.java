package me.johnniang.umami.security;

import me.johnniang.umami.cache.Cache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Security auto configuration.
 *
 * @author johnniang
 */
@Configuration(proxyBeanMethods = false)
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnWebApplication
    FilterRegistrationBean<SecurityFilter> securityFilter(Cache cache) {
        FilterRegistrationBean<SecurityFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SecurityFilter(cache));
        registrationBean.addUrlPatterns("/api/**");
        return registrationBean;
    }
}
