package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.dto.review.ReviewRequest;
import com.blubugtech.bakery_engagement_service.dto.review.ReviewResponse;
import com.blubugtech.bakery_engagement_service.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/engagement/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Review management APIs")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Add a review to a product")
    @PostMapping("/product/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponse> addReview(
            @PathVariable("id") String id,
            @Valid @RequestBody ReviewRequest request) {
        log.info("Add review request received for product ID: {}", id);
        ReviewResponse response = reviewService.addReview(id, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all reviews for a product")
    @GetMapping("/product/{id}")
    public ResponseEntity<org.springframework.data.web.PagedModel<ReviewResponse>> getProductReviews(
            @PathVariable("id") String id, org.springframework.data.domain.Pageable pageable) {
        log.info("Get reviews request received for product ID: {}", id);
        Page<ReviewResponse> reviews = reviewService.getProductReviews(id, pageable);
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(reviews));
    }

    @Operation(summary = "Delete a review")
    @DeleteMapping("/product/{id}/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("id") String productId,
            @PathVariable("reviewId") String reviewId,
            @RequestHeader(value = "X-User-Id", required = false) String headerUserId) {
        
        log.info("Delete review request received for product ID: {}, review ID: {}", productId, reviewId);
        String userId = headerUserId != null ? headerUserId : "current-user-id";
        reviewService.deleteReview(productId, reviewId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Report a review")
    @PostMapping("/product/{id}/{reviewId}/report")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> reportReview(
            @PathVariable("id") String productId,
            @PathVariable("reviewId") String reviewId,
            @RequestBody Map<String, String> request) {
        
        log.info("Report review request received for product ID: {}, review ID: {}", productId, reviewId);
        String reason = request.getOrDefault("reason", "Inappropriate content");
        reviewService.reportReview(productId, reviewId, reason);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get reported reviews")
    @GetMapping("/reported")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<org.springframework.data.web.PagedModel<ReviewResponse>> getReportedReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Get reported reviews request received");
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(reviewService.getReportedReviews(pageable)));
    }

    @Operation(summary = "Dismiss review report")
    @PostMapping("/{reviewId}/dismiss-report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> dismissReviewReport(@PathVariable String reviewId) {
        log.info("Dismiss review report request received for review: {}", reviewId);
        reviewService.dismissReviewReport(reviewId);
        return ResponseEntity.ok().build();
    }
}
