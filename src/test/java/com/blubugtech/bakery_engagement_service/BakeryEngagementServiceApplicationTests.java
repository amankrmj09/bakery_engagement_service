package com.blubugtech.bakery_engagement_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import com.blubugtech.bakery_engagement_service.search.repository.FeedbackSearchRepository;
import com.blubugtech.bakery_engagement_service.search.repository.TestimonialSearchRepository;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
@ActiveProfiles("test")
class BakeryEngagementServiceApplicationTests {

    @MockBean
    private FeedbackSearchRepository feedbackSearchRepository;

    @MockBean
    private TestimonialSearchRepository testimonialSearchRepository;

    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void contextLoads() {
    }

}
