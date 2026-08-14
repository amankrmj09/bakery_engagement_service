package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.dto.feedback.FeedbackRequest;
import com.blubugtech.bakery_engagement_service.dto.feedback.FeedbackResponse;
import com.blubugtech.bakery_engagement_service.entity.Feedback;
import com.blubugtech.bakery_engagement_service.mapper.FeedbackMapper;
import com.blubugtech.bakery_engagement_service.search.document.FeedbackDocument;
import com.blubugtech.bakery_engagement_service.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.blubakery.common.core.dto.RestPageResponse;
@RestController
@RequestMapping("/api/engagement/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Feedback management APIs")
@Slf4j
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;

    @Operation(summary = "Create feedback")
    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(@Valid @RequestBody FeedbackRequest request) {
        log.info("Request received to create feedback for user: {}", request.getName());
        Feedback saved = feedbackService.createFeedback(feedbackMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackMapper.toResponse(saved));
    }

    @Operation(summary = "Get all feedbacks")
    @GetMapping
    public ResponseEntity<RestPageResponse<FeedbackResponse>> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        org.springframework.data.domain.Page<FeedbackResponse> pageResult = feedbackService.getAllFeedbacks(pageable).map(feedbackMapper::toResponse);
        return ResponseEntity.ok(new RestPageResponse<>(pageResult.getContent(), pageResult.getPageable(), pageResult.getTotalElements()));
    }

    @Operation(summary = "Search feedbacks")
    @GetMapping("/search")
    public ResponseEntity<RestPageResponse<FeedbackResponse>> searchFeedbacks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        org.springframework.data.domain.Page<FeedbackResponse> pageResult = feedbackService.searchFeedbacksByUsername(query, type, pageable).map(feedbackMapper::toResponse);
        return ResponseEntity.ok(new RestPageResponse<>(pageResult.getContent(), pageResult.getPageable(), pageResult.getTotalElements()));
    }

    @Operation(summary = "Update feedback status")
    @PutMapping("/{id}/status")
    public ResponseEntity<FeedbackResponse> updateFeedbackStatus(@PathVariable String id, @RequestParam String status) {
        return ResponseEntity.ok(feedbackMapper.toResponse(feedbackService.updateFeedbackStatus(id, status)));
    }

    @Operation(summary = "Delete feedback")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable String id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}
