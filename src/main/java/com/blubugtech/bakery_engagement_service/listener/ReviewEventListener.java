package com.blubugtech.bakery_engagement_service.listener;

import com.blubugtech.bakery_engagement_service.entity.Review;
import com.blubugtech.bakery_engagement_service.event.ReviewDomainEvent;
import com.blubugtech.bakery_engagement_service.repository.ReviewRepository;
import org.blubakery.common.messaging.constants.KafkaTopics;
import org.blubakery.common.messaging.contract.messaging.ReviewPayload;
import org.blubakery.common.messaging.event.ReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewEventListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ReviewRepository reviewRepository;

    @Async
    @EventListener
    public void handleReviewDomainEvent(ReviewDomainEvent event) {
        Review review = event.getReview();
        String action = event.getAction();
        
        try {
            List<Review> allReviews = reviewRepository.findByProductId(review.getProductId());
            int totalReviews = allReviews.size();
            double sum = allReviews.stream().mapToInt(Review::getRating).sum();
            double avg = totalReviews > 0 ? sum / totalReviews : 0.0;
            double roundedAvg = Math.round(avg * 10.0) / 10.0;

            ReviewPayload payload = ReviewPayload.builder()
                    .reviewId(review.getId())
                    .productId(review.getProductId())
                    .userId(review.getUserId())
                    .userName(review.getUserName())
                    .rating(review.getRating())
                    .comment(review.getComment())
                    .timestamp(LocalDateTime.now())
                    .action(action)
                    .averageRating(roundedAvg)
                    .totalReviews(totalReviews)
                    .build();

            ReviewEvent kafkaEvent = new ReviewEvent();
            kafkaEvent.setPayload(payload);
            kafkaEvent.setEventId(UUID.randomUUID().toString());
            kafkaEvent.setTimestamp(Instant.now());

            kafkaTemplate.send(KafkaTopics.REVIEWS_TOPIC, kafkaEvent.getEventId(), kafkaEvent);
            log.info("Published review {} event for product {}", action, review.getProductId());
        } catch (Exception e) {
            log.error("Failed to publish review event for product {}", review.getProductId(), e);
        }
    }
}
