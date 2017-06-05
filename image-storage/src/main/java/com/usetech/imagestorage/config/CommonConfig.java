package com.usetech.imagestorage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by User on 14.11.2016.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class CommonConfig {

    @Value("${com.usetech.imagestorage.rootDir}")
    private String imageRootDir;

    @Value("${com.usetech.logstorage.rootDir}")
    private String logRootDir;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public String getImageRootDir() {
        return imageRootDir;
    }

    public String getLogRootDir() {
        return logRootDir;
    }
}
