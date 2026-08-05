package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.dto.review.ReviewRequest;
import com.blubugtech.bakery_engagement_service.dto.review.ReviewResponse;
import com.blubugtech.bakery_engagement_service.dto.review.ReviewUpdateRequest;
import com.blubugtech.bakery_engagement_service.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/engagement/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Review management APIs for customers")
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

    @Operation(summary = "Update an existing review")
    @PutMapping("/product/{id}/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable("id") String productId,
            @PathVariable("reviewId") String reviewId,
            @RequestHeader(value = "X-User-Id", required = false) String headerUserId,
            @Valid @RequestBody ReviewUpdateRequest request) {
        
        log.info("Update review request received for product ID: {}, review ID: {}", productId, reviewId);
        String userId = headerUserId != null ? headerUserId : "current-user-id";
        ReviewResponse response = reviewService.updateReview(productId, reviewId, userId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all reviews for a product")
    @GetMapping("/product/{id}")
    public ResponseEntity<org.springframework.data.web.PagedModel<ReviewResponse>> getProductReviews(
            @PathVariable("id") String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.info("Get reviews request received for product ID: {}", id);
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        return ResponseEntity.ok(reviewService.getProductReviews(id, pageable));
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
}
