package com.blubugtech.bakery_engagement_service.service;

import com.blubugtech.bakery_engagement_service.dto.review.ReviewRequest;
import com.blubugtech.bakery_engagement_service.dto.review.ReviewResponse;
import com.blubugtech.bakery_engagement_service.dto.review.ReviewUpdateRequest;
import com.blubugtech.bakery_engagement_service.entity.Review;
import com.blubugtech.bakery_engagement_service.repository.ReviewRepository;
import com.blubugtech.common.constants.KafkaTopics;
import com.blubugtech.common.contract.messaging.ReviewPayload;
import com.blubugtech.common.event.ReviewEvent;
import com.blubugtech.common.exception.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public ReviewResponse addReview(String productId, ReviewRequest request) {
        Optional<Review> existingReviewOpt = reviewRepository.findByProductIdAndOrderId(productId, request.getOrderId());
        Review review;
        if (existingReviewOpt.isPresent()) {
            review = existingReviewOpt.get();
            review.setRating(request.getRating());
            review.setComment(request.getComment());
            review.setUpdatedAt(LocalDateTime.now());
        } else {
            review = Review.builder()
                    .productId(productId)
                    .orderId(request.getOrderId())
                    .userId(request.getUserId())
                    .userName(request.getUserName())
                    .rating(request.getRating())
                    .comment(request.getComment())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        review = reviewRepository.save(review);
        publishReviewEvent(review, "CREATED");

        return ReviewResponse.fromEntity(review);
    }

    @Transactional
    public ReviewResponse updateReview(String productId, String reviewId, String userId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        
        if (!review.getProductId().equals(productId)) {
            throw new IllegalArgumentException("Review does not belong to the specified product");
        }
        
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUpdatedAt(LocalDateTime.now());
        
        review = reviewRepository.save(review);
        publishReviewEvent(review, "UPDATED");
        
        return ReviewResponse.fromEntity(review);
    }

    public Page<ReviewResponse> getProductReviews(String productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable)
                .map(ReviewResponse::fromEntity);
    }

    @Transactional
    public void deleteReview(String productId, String reviewId, String userId) {
        Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        
        if (!review.getProductId().equals(productId)) {
            throw new IllegalArgumentException("Review does not belong to the specified product");
        }
        
        reviewRepository.delete(review);
        publishReviewEvent(review, "DELETED");
    }

    @Transactional
    public void reportReview(String productId, String reviewId, String reason) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        if (!review.getProductId().equals(productId)) {
            throw new IllegalArgumentException("Review does not belong to the specified product");
        }

        review.setIsReported(true);
        review.setReportReason(reason);
        review.setReportedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    public Page<ReviewResponse> getReportedReviews(Pageable pageable) {
        return reviewRepository.findByIsReportedTrue(pageable)
                .map(ReviewResponse::fromEntity);
    }

    @Transactional
    public void dismissReviewReport(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        review.setIsReported(false);
        review.setReportReason(null);
        review.setReportedAt(null);
        reviewRepository.save(review);
    }

    private void publishReviewEvent(Review review, String action) {
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

            ReviewEvent event = new ReviewEvent();
            event.setPayload(payload);
            event.setEventId(UUID.randomUUID().toString());
            event.setTimestamp(Instant.now());

            kafkaTemplate.send(KafkaTopics.REVIEWS_TOPIC, event.getEventId(), event);
            log.info("Published review {} event for product {}", action, review.getProductId());
        } catch (Exception e) {
            log.error("Failed to publish review event for product {}", review.getProductId(), e);
        }
    }
}
