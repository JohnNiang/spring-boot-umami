package me.johnniang.umami.config;

import me.johnniang.umami.cache.Cache;
import me.johnniang.umami.cache.InMemoryCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Umami configuration.
 *
 * @author johnniang
 */
@Configuration(proxyBeanMethods = false)
public class UmamiConfiguration {

//    @Bean
//    HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
//        return hibernateProperties -> hibernateProperties.put("hibernate.metadata_builder_contributor",
//                (MetadataBuilderContributor) metadataBuilder -> {
//                    metadataBuilder.applySqlFunction("formatdatetime", new StandardSQLFunction("formatdatetime", LocalDateTimeType.INSTANCE));
//                });
//    }

    @Bean
    @ConditionalOnMissingBean
    Cache cache() {
        return new InMemoryCache();
    }
}
