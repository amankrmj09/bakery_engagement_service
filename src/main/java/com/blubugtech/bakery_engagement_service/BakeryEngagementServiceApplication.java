package com.blubugtech.bakery_engagement_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import com.blubugtech.common.security.MethodSecurityConfig;
import com.blubugtech.common.kafka.KafkaConfig;

@SpringBootApplication
@Import({MethodSecurityConfig.class, KafkaConfig.class})
public class BakeryEngagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BakeryEngagementServiceApplication.class, args);
    }

}
