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
import org.blubakery.common.core.dto.RestPageResponse;
@Slf4j
@RestController
@RequestMapping("/api/admin/engagement/reviews")
@RequiredArgsConstructor
@Tag(name = "Admin Reviews", description = "Review management APIs for admins")
public class AdminReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Get reported reviews")
    @GetMapping("/reported")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestPageResponse<ReviewResponse>> getReportedReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.info("Get reported reviews request received");
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        org.springframework.data.domain.Page<ReviewResponse> pageResult = reviewService.getReportedReviews(pageable);
        return ResponseEntity.ok(new RestPageResponse<>(pageResult.getContent(), pageResult.getPageable(), pageResult.getTotalElements()));
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
