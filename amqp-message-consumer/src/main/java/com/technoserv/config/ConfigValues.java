package com.technoserv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 */
@Configuration
@PropertySource("classpath:application.properties")
public class ConfigValues {

    public static final String ARM_QUEUE_MAX_NUMBER_OF_RETRIES = "${queue.arm-retry.max-number-of-retries}";

    public static final String API_COMPARE = "${api.compare.skud}";

    public static final String BROKER_URL = "${com.technoserv.queues.broker-url}";

    public static final String SKUD_NOTIFICATION_QUEUE = "${com.technoserv.queues.skud-notification-queue}";

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
