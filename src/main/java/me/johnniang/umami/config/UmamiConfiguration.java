package me.johnniang.umami.config;

import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration(proxyBeanMethods = false)
public class UmamiConfiguration {

    @Bean
    HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> hibernateProperties.put("hibernate.metadata_builder_contributor",
                (MetadataBuilderContributor) metadataBuilder -> {
                    metadataBuilder.applySqlFunction("formatdatetime", new StandardSQLFunction("formatdatetime", StandardBasicTypes.TIMESTAMP));
                });
    }
}
