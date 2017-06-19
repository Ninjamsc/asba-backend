package com.technoserv.config;

/**
 *
 */
public class ConfigValues {

    public static final String ARM_QUEUE_MAX_NUMBER_OF_RETRIES = "${queue.arm-retry.max-number-of-retries}";

    public static final String API_COMPARE = "${api.compare.skud}";

    public static final String BROKER_URL = "${com.technoserv.queues.brokerUrl}";

    public static final String SKUD_NOTIFICATION_QUEUE = "${com.technoserv.queues.skud-notification-queue}";

    private ConfigValues() {

    }
}
