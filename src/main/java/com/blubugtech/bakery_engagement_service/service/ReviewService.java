package com.blubugtech.bakery_engagement_service.service;

import com.blubugtech.bakery_engagement_service.dto.review.ReviewRequest;
import com.blubugtech.bakery_engagement_service.dto.review.ReviewResponse;
import com.blubugtech.bakery_engagement_service.dto.review.ReviewUpdateRequest;
import com.blubugtech.bakery_engagement_service.entity.Review;
import com.blubugtech.bakery_engagement_service.event.ReviewDomainEvent;
import com.blubugtech.bakery_engagement_service.repository.ReviewRepository;
import org.blubakery.common.core.exception.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ApplicationEventPublisher eventPublisher;

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
        eventPublisher.publishEvent(new ReviewDomainEvent(this, review, "CREATED"));

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
        eventPublisher.publishEvent(new ReviewDomainEvent(this, review, "UPDATED"));
        
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
        eventPublisher.publishEvent(new ReviewDomainEvent(this, review, "DELETED"));
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
}
