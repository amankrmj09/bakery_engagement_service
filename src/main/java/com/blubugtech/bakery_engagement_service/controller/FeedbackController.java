package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import com.blubugtech.bakery_engagement_service.search.document.FeedbackDocument;
import com.blubugtech.bakery_engagement_service.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/engagement/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Feedback management APIs")
@Slf4j
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "Create feedback")
    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        log.info("Request received to create feedback for user: {}", feedback.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.createFeedback(feedback));
    }

    @Operation(summary = "Get all feedbacks")
    @GetMapping
    public ResponseEntity<org.springframework.data.web.PagedModel<Feedback>> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(feedbackService.getAllFeedbacks(page, size)));
    }

    @Operation(summary = "Search feedbacks")
    @GetMapping("/search")
    public ResponseEntity<org.springframework.data.web.PagedModel<FeedbackDocument>> searchFeedbacks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(feedbackService.searchFeedbacksByUsername(query, type, page, size)));
    }

    @Operation(summary = "Update feedback status")
    @PutMapping("/{id}/status")
    public ResponseEntity<Feedback> updateFeedbackStatus(@PathVariable String id, @RequestParam String status) {
        return ResponseEntity.ok(feedbackService.updateFeedbackStatus(id, status));
    }

    @Operation(summary = "Delete feedback")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable String id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}
