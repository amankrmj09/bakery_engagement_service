package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.dto.review.ReviewResponse;
import com.blubugtech.bakery_engagement_service.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/engagement/reviews")
@RequiredArgsConstructor
@Tag(name = "Admin Reviews", description = "Review management APIs for admins")
public class AdminReviewController {

    private final ReviewService reviewService;

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
