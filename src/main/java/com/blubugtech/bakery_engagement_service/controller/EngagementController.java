package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import com.blubugtech.bakery_engagement_service.entity.ContactDetails;
import com.blubugtech.bakery_engagement_service.search.document.FeedbackDocument;
import com.blubugtech.bakery_engagement_service.search.document.TestimonialDocument;
import com.blubugtech.bakery_engagement_service.service.EngagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/engagement")
@RequiredArgsConstructor
@Tag(name = "Engagement", description = "Engagement management APIs")
public class EngagementController {

    private final EngagementService engagementService;

    @Operation(summary = "Create a testimonial")
    @PostMapping("/testimonials")
    public ResponseEntity<Testimonial> createTestimonial(@RequestBody Testimonial testimonial) {
        return ResponseEntity.status(HttpStatus.CREATED).body(engagementService.createTestimonial(testimonial));
    }

    @Operation(summary = "Get all testimonials")
    @GetMapping("/testimonials")
    public ResponseEntity<org.springframework.data.web.PagedModel<Testimonial>> getAllTestimonials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(engagementService.getAllTestimonials(page, size)));
    }

    @Operation(summary = "Get featured testimonials")
    @GetMapping("/testimonials/featured")
    public ResponseEntity<org.springframework.data.web.PagedModel<Testimonial>> getFeaturedTestimonials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(engagementService.getFeaturedTestimonials(page, size)));
    }

    @Operation(summary = "Search testimonials")
    @GetMapping("/testimonials/search")
    public ResponseEntity<org.springframework.data.web.PagedModel<TestimonialDocument>> searchTestimonials(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(engagementService.searchTestimonialsByUsername(username, page, size)));
    }

    @Operation(summary = "Toggle featured status of a testimonial")
    @PutMapping("/testimonials/{id}/feature")
    public ResponseEntity<?> toggleFeatured(@PathVariable String id, @RequestParam boolean featured) {
        try {
            return ResponseEntity.ok(engagementService.toggleFeatured(id, featured));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete a testimonial")
    @DeleteMapping("/testimonials/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable String id) {
        engagementService.deleteTestimonial(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create feedback")
    @PostMapping("/feedback")
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        return ResponseEntity.status(HttpStatus.CREATED).body(engagementService.createFeedback(feedback));
    }

    @Operation(summary = "Get all feedbacks")
    @GetMapping("/feedback")
    public ResponseEntity<org.springframework.data.web.PagedModel<Feedback>> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(engagementService.getAllFeedbacks(page, size)));
    }

    @Operation(summary = "Search feedbacks")
    @GetMapping("/feedback/search")
    public ResponseEntity<org.springframework.data.web.PagedModel<FeedbackDocument>> searchFeedbacks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(engagementService.searchFeedbacksByUsername(query, type, page, size)));
    }

    @Operation(summary = "Delete feedback")
    @DeleteMapping("/feedback/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable String id) {
        engagementService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update feedback status")
    @PutMapping("/feedback/{id}/status")
    public ResponseEntity<Feedback> updateFeedbackStatus(@PathVariable String id, @RequestParam String status) {
        return ResponseEntity.ok(engagementService.updateFeedbackStatus(id, status));
    }

    @Operation(summary = "Get contact details")
    @GetMapping("/contact-details")
    public ResponseEntity<ContactDetails> getContactDetails() {
        return ResponseEntity.ok(engagementService.getContactDetails());
    }

    @Operation(summary = "Update contact details")
    @PutMapping("/contact-details")
    public ResponseEntity<ContactDetails> updateContactDetails(@RequestBody ContactDetails request) {
        return ResponseEntity.ok(engagementService.updateContactDetails(request));
    }
}
