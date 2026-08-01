package com.blubugtech.bakery_engagement_service.listener;

import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import com.blubugtech.bakery_engagement_service.event.TestimonialDomainEvent;
import com.blubugtech.bakery_engagement_service.search.document.TestimonialDocument;
import com.blubugtech.bakery_engagement_service.search.repository.TestimonialSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blubakery.common.messaging.constants.KafkaTopics;
import org.blubakery.common.messaging.contract.messaging.FeedbackPayload;
import org.blubakery.common.messaging.event.FeedbackEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestimonialEventListener {

    private final TestimonialSearchRepository testimonialSearchRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @EventListener
    public void handleTestimonialEvent(TestimonialDomainEvent event) {
        Testimonial testimonial = event.getTestimonial();
        String action = event.getAction();
        log.info("Handling TestimonialDomainEvent for action: {}", action);

        if ("CREATED".equals(action) || "UPDATED".equals(action)) {
            try {
                TestimonialDocument doc = TestimonialDocument.builder()
                        .id(testimonial.getId())
                        .uid(testimonial.getUid())
                        .name(testimonial.getName())
                        .profileImageUrl(testimonial.getProfileImageUrl())
                        .rating(testimonial.getRating())
                        .message(testimonial.getMessage())
                        .isFeatured(testimonial.getIsFeatured())
                        .status(testimonial.getStatus())
                        .build();
                testimonialSearchRepository.save(doc);
                log.debug("Indexed testimonial {} into Elasticsearch", testimonial.getId());
            } catch (Exception e) {
                log.error("Failed to index testimonial into Elasticsearch: {}", e.getMessage(), e);
            }
        } else if ("DELETED".equals(action)) {
            try {
                testimonialSearchRepository.deleteById(testimonial.getId());
                log.debug("Deleted testimonial {} from Elasticsearch", testimonial.getId());
            } catch (Exception e) {
                log.error("Could not delete testimonial from Elasticsearch for id {}: {}", testimonial.getId(), e.getMessage(), e);
            }
        }

        if ("CREATED".equals(action)) {
            try {
                FeedbackPayload payload = FeedbackPayload.builder()
                    .feedbackId(UUID.randomUUID())
                    .firstName(testimonial.getName())
                    .customerEmail(testimonial.getEmail())
                    .type("TESTIMONIAL")
                    .rating(testimonial.getRating())
                    .timestamp(LocalDateTime.now())
                    .build();
                    
                FeedbackEvent kafkaEvent = new FeedbackEvent();
                kafkaEvent.setPayload(payload);
                kafkaEvent.setEventId(UUID.randomUUID().toString());
                kafkaEvent.setTimestamp(java.time.Instant.now());

                kafkaTemplate.send(KafkaTopics.FEEDBACK_TOPIC, kafkaEvent.getEventId(), kafkaEvent);
                log.info("Sent testimonial notification event for: {}", testimonial.getName());
            } catch (Exception e) {
                log.error("Failed to send Kafka event for testimonial: {}", e.getMessage(), e);
            }
        }
    }
}
