package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import com.blubugtech.bakery_engagement_service.search.document.TestimonialDocument;
import com.blubugtech.bakery_engagement_service.service.TestimonialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/engagement/testimonials")
@RequiredArgsConstructor
@Tag(name = "Testimonial", description = "Testimonial management APIs")
@Slf4j
public class TestimonialController {

    private final TestimonialService testimonialService;

    @Operation(summary = "Create a testimonial")
    @PostMapping
    public ResponseEntity<Testimonial> createTestimonial(@RequestBody Testimonial testimonial) {
        log.info("Request received to create testimonial for user: {}", testimonial.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(testimonialService.createTestimonial(testimonial));
    }

    @Operation(summary = "Get all testimonials")
    @GetMapping
    public ResponseEntity<org.springframework.data.web.PagedModel<Testimonial>> getAllTestimonials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(testimonialService.getAllTestimonials(page, size)));
    }

    @Operation(summary = "Get featured testimonials")
    @GetMapping("/featured")
    public ResponseEntity<org.springframework.data.web.PagedModel<Testimonial>> getFeaturedTestimonials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(testimonialService.getFeaturedTestimonials(page, size)));
    }

    @Operation(summary = "Search testimonials")
    @GetMapping("/search")
    public ResponseEntity<org.springframework.data.web.PagedModel<TestimonialDocument>> searchTestimonials(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new org.springframework.data.web.PagedModel<>(testimonialService.searchTestimonialsByUsername(username, page, size)));
    }

    @Operation(summary = "Toggle featured status of a testimonial")
    @PutMapping("/{id}/feature")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable String id) {
        testimonialService.deleteTestimonial(id);
        return ResponseEntity.noContent().build();
    }
}
