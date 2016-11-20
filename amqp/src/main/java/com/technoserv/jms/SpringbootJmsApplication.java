package com.technoserv.jms;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Queue;

@SpringBootApplication
@EnableJms
public class SpringbootJmsApplication {

    @Bean
    public Queue queue() {
        return new ActiveMQQueue("internal.queue");
    }

    @Bean
    public Queue externalQueue() {
        return new ActiveMQQueue("external.queue");
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJmsApplication.class, args);

    }
}
