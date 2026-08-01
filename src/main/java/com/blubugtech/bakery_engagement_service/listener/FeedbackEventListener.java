package com.blubugtech.bakery_engagement_service.listener;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import com.blubugtech.bakery_engagement_service.event.FeedbackDomainEvent;
import com.blubugtech.bakery_engagement_service.search.document.FeedbackDocument;
import com.blubugtech.bakery_engagement_service.search.repository.FeedbackSearchRepository;
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
public class FeedbackEventListener {

    private final FeedbackSearchRepository feedbackSearchRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @EventListener
    public void handleFeedbackEvent(FeedbackDomainEvent event) {
        Feedback feedback = event.getFeedback();
        String action = event.getAction();
        log.info("Handling FeedbackDomainEvent for action: {}", action);

        if ("CREATED".equals(action) || "STATUS_UPDATED".equals(action)) {
            try {
                FeedbackDocument doc = FeedbackDocument.builder()
                        .id(feedback.getId())
                        .uid(feedback.getUid())
                        .name(feedback.getName())
                        .email(feedback.getEmail())
                        .type(feedback.getType())
                        .message(feedback.getMessage())
                        .status(feedback.getStatus())
                        .build();
                feedbackSearchRepository.save(doc);
                log.debug("Indexed feedback {} into Elasticsearch", feedback.getId());
            } catch (Exception e) {
                log.error("Failed to index feedback into Elasticsearch: {}", e.getMessage(), e);
            }
        } else if ("DELETED".equals(action)) {
            try {
                feedbackSearchRepository.deleteById(feedback.getId());
                log.debug("Deleted feedback {} from Elasticsearch", feedback.getId());
            } catch (Exception e) {
                log.error("Could not delete feedback from Elasticsearch for id {}: {}", feedback.getId(), e.getMessage(), e);
            }
        }

        if ("CREATED".equals(action)) {
            try {
                FeedbackPayload payload = FeedbackPayload.builder()
                    .feedbackId(UUID.randomUUID())
                    .firstName(feedback.getName())
                    .customerEmail(feedback.getEmail())
                    .type(feedback.getType() != null ? feedback.getType() : "FEEDBACK")
                    .timestamp(LocalDateTime.now())
                    .build();
                    
                FeedbackEvent kafkaEvent = new FeedbackEvent();
                kafkaEvent.setPayload(payload);
                kafkaEvent.setEventId(UUID.randomUUID().toString());
                kafkaEvent.setTimestamp(java.time.Instant.now());

                kafkaTemplate.send(KafkaTopics.FEEDBACK_TOPIC, kafkaEvent.getEventId(), kafkaEvent);
                log.info("Sent feedback notification event for: {}", feedback.getName());
            } catch (Exception e) {
                log.error("Failed to send Kafka event for feedback: {}", e.getMessage(), e);
            }
        }
    }
}
