package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import com.blubugtech.bakery_engagement_service.entity.ContactDetails;
import com.blubugtech.bakery_engagement_service.search.document.FeedbackDocument;
import com.blubugtech.bakery_engagement_service.search.document.TestimonialDocument;
import com.blubugtech.bakery_engagement_service.service.EngagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/engagement")
@RequiredArgsConstructor
public class EngagementController {

    private final EngagementService engagementService;

    @PostMapping("/testimonials")
    public ResponseEntity<Testimonial> createTestimonial(@RequestBody Testimonial testimonial) {
        return ResponseEntity.status(HttpStatus.CREATED).body(engagementService.createTestimonial(testimonial));
    }

    @GetMapping("/testimonials")
    public ResponseEntity<org.springframework.data.web.PagedModel<Testimonial>> getAllTestimonials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(engagementService.getAllTestimonials(page, size)));
    }

    @GetMapping("/testimonials/featured")
    public ResponseEntity<org.springframework.data.web.PagedModel<Testimonial>> getFeaturedTestimonials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(engagementService.getFeaturedTestimonials(page, size)));
    }

    @GetMapping("/testimonials/search")
    public ResponseEntity<org.springframework.data.web.PagedModel<TestimonialDocument>> searchTestimonials(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(engagementService.searchTestimonialsByUsername(username, page, size)));
    }

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

    @PostMapping("/feedback")
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        return ResponseEntity.status(HttpStatus.CREATED).body(engagementService.createFeedback(feedback));
    }

    @GetMapping("/feedback")
    public ResponseEntity<org.springframework.data.web.PagedModel<Feedback>> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(engagementService.getAllFeedbacks(page, size)));
    }

    @GetMapping("/feedback/search")
    public ResponseEntity<org.springframework.data.web.PagedModel<FeedbackDocument>> searchFeedbacks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(engagementService.searchFeedbacksByUsername(query, type, page, size)));
    }

    @GetMapping("/contact-details")
    public ResponseEntity<ContactDetails> getContactDetails() {
        return ResponseEntity.ok(engagementService.getContactDetails());
    }

    @PutMapping("/contact-details")
    public ResponseEntity<ContactDetails> updateContactDetails(@RequestBody ContactDetails request) {
        return ResponseEntity.ok(engagementService.updateContactDetails(request));
    }
}
