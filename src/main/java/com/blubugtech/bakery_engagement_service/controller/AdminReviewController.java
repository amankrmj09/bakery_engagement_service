package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.dto.review.ReviewResponse;
import com.blubugtech.bakery_engagement_service.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/engagement/reviews")
@RequiredArgsConstructor
@Tag(name = "Admin Reviews", description = "Review management APIs for admins")
@Slf4j
public class AdminReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Get all reported reviews")
    @GetMapping("/reported")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<org.springframework.data.web.PagedModel<ReviewResponse>> getReportedReviews(Pageable pageable) {
        log.info("Admin request received to get reported reviews");
        Page<ReviewResponse> reviews = reviewService.getReportedReviews(pageable);
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(reviews));
    }

    @Operation(summary = "Dismiss a review report")
    @PutMapping("/{reviewId}/dismiss-report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> dismissReviewReport(@PathVariable String reviewId) {
        log.info("Admin request received to dismiss report for review ID: {}", reviewId);
        reviewService.dismissReviewReport(reviewId);
        return ResponseEntity.ok().build();
    }
}
