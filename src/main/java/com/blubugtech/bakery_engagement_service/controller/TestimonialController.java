package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.dto.testimonial.TestimonialRequest;
import com.blubugtech.bakery_engagement_service.dto.testimonial.TestimonialResponse;
import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import com.blubugtech.bakery_engagement_service.mapper.TestimonialMapper;
import com.blubugtech.bakery_engagement_service.search.document.TestimonialDocument;
import com.blubugtech.bakery_engagement_service.service.TestimonialService;
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
@RequestMapping("/api/engagement/testimonials")
@RequiredArgsConstructor
@Tag(name = "Testimonial", description = "Testimonial management APIs")
@Slf4j
public class TestimonialController {

    private final TestimonialService testimonialService;
    private final TestimonialMapper testimonialMapper;

    @Operation(summary = "Create a testimonial")
    @PostMapping
    public ResponseEntity<TestimonialResponse> createTestimonial(@Valid @RequestBody TestimonialRequest request) {
        log.info("Request received to create testimonial for user: {}", request.getAuthorName());
        Testimonial saved = testimonialService.createTestimonial(testimonialMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(testimonialMapper.toResponse(saved));
    }

    @Operation(summary = "Get all testimonials")
    @GetMapping
    public ResponseEntity<RestPageResponse<TestimonialResponse>> getAllTestimonials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        org.springframework.data.domain.Page<TestimonialResponse> pageResult = testimonialService.getAllTestimonials(pageable).map(testimonialMapper::toResponse);
        return ResponseEntity.ok(new RestPageResponse<>(pageResult.getContent(), pageResult.getPageable(), pageResult.getTotalElements()));
    }

    @Operation(summary = "Get featured testimonials")
    @GetMapping("/featured")
    public ResponseEntity<RestPageResponse<TestimonialResponse>> getFeaturedTestimonials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        org.springframework.data.domain.Page<TestimonialResponse> pageResult = testimonialService.getFeaturedTestimonials(pageable).map(testimonialMapper::toResponse);
        return ResponseEntity.ok(new RestPageResponse<>(pageResult.getContent(), pageResult.getPageable(), pageResult.getTotalElements()));
    }

    @Operation(summary = "Search testimonials")
    @GetMapping("/search")
    public ResponseEntity<RestPageResponse<TestimonialResponse>> searchTestimonials(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        org.springframework.data.domain.Page<TestimonialResponse> pageResult = testimonialService.searchTestimonialsByUsername(username, pageable).map(testimonialMapper::toResponse);
        return ResponseEntity.ok(new RestPageResponse<>(pageResult.getContent(), pageResult.getPageable(), pageResult.getTotalElements()));
    }

    @Operation(summary = "Toggle featured status of a testimonial")
    @PutMapping("/{id}/feature")
    public ResponseEntity<TestimonialResponse> toggleFeatured(@PathVariable String id, @RequestParam boolean featured) {
        return ResponseEntity.ok(testimonialMapper.toResponse(testimonialService.toggleFeatured(id, featured)));
    }

    @Operation(summary = "Delete a testimonial")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable String id) {
        testimonialService.deleteTestimonial(id);
        return ResponseEntity.noContent().build();
    }
}
