package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import com.blubugtech.bakery_engagement_service.entity.ContactDetails;
import com.blubugtech.bakery_engagement_service.search.document.FeedbackDocument;
import com.blubugtech.bakery_engagement_service.search.document.TestimonialDocument;
import com.blubugtech.bakery_engagement_service.service.ContactDetailsService;
import com.blubugtech.bakery_engagement_service.service.FeedbackService;
import com.blubugtech.bakery_engagement_service.service.TestimonialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/engagement")
@RequiredArgsConstructor
@Tag(name = "Engagement", description = "Engagement management APIs")
@Slf4j
public class EngagementController {

    private final TestimonialService testimonialService;
    private final FeedbackService feedbackService;
    private final ContactDetailsService contactDetailsService;

    @Operation(summary = "Create a testimonial")
    @PostMapping("/testimonials")
    public ResponseEntity<Testimonial> createTestimonial(@RequestBody Testimonial testimonial) {
        log.info("Request received to create testimonial for user: {}", testimonial.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(testimonialService.createTestimonial(testimonial));
    }

    @Operation(summary = "Get all testimonials")
    @GetMapping("/testimonials")
    public ResponseEntity<org.springframework.data.web.PagedModel<Testimonial>> getAllTestimonials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        return ResponseEntity.ok(testimonialService.getAllTestimonials(pageable));
    }

    @Operation(summary = "Get featured testimonials")
    @GetMapping("/testimonials/featured")
    public ResponseEntity<org.springframework.data.web.PagedModel<Testimonial>> getFeaturedTestimonials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        return ResponseEntity.ok(testimonialService.getFeaturedTestimonials(pageable));
    }

    @Operation(summary = "Search testimonials")
    @GetMapping("/testimonials/search")
    public ResponseEntity<org.springframework.data.web.PagedModel<TestimonialDocument>> searchTestimonials(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        return ResponseEntity.ok(testimonialService.searchTestimonialsByUsername(username, pageable));
    }

    @Operation(summary = "Toggle featured status of a testimonial")
    @PutMapping("/testimonials/{id}/feature")
    public ResponseEntity<?> toggleFeatured(@PathVariable String id, @RequestParam boolean featured) {
        try {
            return ResponseEntity.ok(testimonialService.toggleFeatured(id, featured));
        } catch (IllegalStateException e) {
            log.error("Illegal state while toggling featured status", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument while toggling featured status", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete a testimonial")
    @DeleteMapping("/testimonials/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable String id) {
        testimonialService.deleteTestimonial(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create feedback")
    @PostMapping("/feedback")
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        log.info("Request received to create feedback for user: {}", feedback.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.createFeedback(feedback));
    }

    @Operation(summary = "Get all feedbacks")
    @GetMapping("/feedback")
    public ResponseEntity<org.springframework.data.web.PagedModel<Feedback>> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        return ResponseEntity.ok(feedbackService.getAllFeedbacks(pageable));
    }

    @Operation(summary = "Search feedbacks")
    @GetMapping("/feedback/search")
    public ResponseEntity<org.springframework.data.web.PagedModel<FeedbackDocument>> searchFeedbacks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        return ResponseEntity.ok(feedbackService.searchFeedbacksByUsername(query, type, pageable));
    }

    @Operation(summary = "Delete feedback")
    @DeleteMapping("/feedback/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable String id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update feedback status")
    @PutMapping("/feedback/{id}/status")
    public ResponseEntity<Feedback> updateFeedbackStatus(@PathVariable String id, @RequestParam String status) {
        return ResponseEntity.ok(feedbackService.updateFeedbackStatus(id, status));
    }

    @Operation(summary = "Get contact details")
    @GetMapping("/contact-details")
    public ResponseEntity<ContactDetails> getContactDetails() {
        return ResponseEntity.ok(contactDetailsService.getContactDetails());
    }

    @Operation(summary = "Update contact details")
    @PutMapping("/contact-details")
    public ResponseEntity<ContactDetails> updateContactDetails(@RequestBody ContactDetails request) {
        return ResponseEntity.ok(contactDetailsService.updateContactDetails(request));
    }
}
