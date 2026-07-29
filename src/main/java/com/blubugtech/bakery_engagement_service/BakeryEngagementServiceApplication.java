package com.blubugtech.bakery_engagement_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.blubakery.common.security.security.MethodSecurityConfig;
import org.blubakery.common.messaging.kafka.KafkaConfig;

@SpringBootApplication
@Import({MethodSecurityConfig.class, KafkaConfig.class})
public class BakeryEngagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BakeryEngagementServiceApplication.class, args);
    }

}
