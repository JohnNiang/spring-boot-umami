package me.johnniang.umami.config;

import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UmamiConfiguration {

//    @Bean
//    HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
//        return hibernateProperties -> hibernateProperties.put("hibernate.metadata_builder_contributor",
//                (MetadataBuilderContributor) metadataBuilder -> {
//                    metadataBuilder.applySqlFunction("formatdatetime", new StandardSQLFunction("formatdatetime", LocalDateTimeType.INSTANCE));
//                });
//    }
}
