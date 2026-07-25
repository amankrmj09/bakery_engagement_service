package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import com.blubugtech.bakery_engagement_service.entity.Testimonial;
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
    public ResponseEntity<List<Testimonial>> getAllTestimonials() {
        return ResponseEntity.ok(engagementService.getAllTestimonials());
    }

    @GetMapping("/testimonials/featured")
    public ResponseEntity<List<Testimonial>> getFeaturedTestimonials() {
        return ResponseEntity.ok(engagementService.getFeaturedTestimonials());
    }

    @GetMapping("/testimonials/search")
    public ResponseEntity<List<TestimonialDocument>> searchTestimonials(@RequestParam(required = false) String username) {
        return ResponseEntity.ok(engagementService.searchTestimonialsByUsername(username));
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
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        return ResponseEntity.ok(engagementService.getAllFeedbacks());
    }

    @GetMapping("/feedback/search")
    public ResponseEntity<List<FeedbackDocument>> searchFeedbacks(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(engagementService.searchFeedbacksByUsername(query));
    }
}
