package com.blubugtech.bakery_engagement_service.service;

import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import com.blubugtech.bakery_engagement_service.event.TestimonialDomainEvent;
import com.blubugtech.bakery_engagement_service.repository.TestimonialRepository;
import com.blubugtech.bakery_engagement_service.search.document.TestimonialDocument;
import com.blubugtech.bakery_engagement_service.search.repository.TestimonialSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestimonialService {

    private final TestimonialRepository testimonialRepository;
    private final TestimonialSearchRepository testimonialSearchRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Testimonial createTestimonial(Testimonial testimonial) {
        log.info("Creating new testimonial for user: {}", testimonial.getName());
        if (testimonial.getCreatedAt() == null) {
            testimonial.setCreatedAt(LocalDateTime.now());
        }
        testimonial.setUpdatedAt(LocalDateTime.now());
        Testimonial saved = testimonialRepository.save(testimonial);
        
        eventPublisher.publishEvent(new TestimonialDomainEvent(this, saved, "CREATED"));
        return saved;
    }

    @Transactional
    public Testimonial toggleFeatured(String id, boolean featured) {
        Testimonial testimonial = testimonialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Testimonial not found: " + id));

        if (featured && !Boolean.TRUE.equals(testimonial.getIsFeatured())) {
            List<Testimonial> currentlyFeatured = testimonialRepository.findByIsFeaturedTrue();
            if (currentlyFeatured.size() >= 5) {
                throw new IllegalStateException("Cannot feature more than 5 testimonials. Please unfeature an existing one first.");
            }
        }

        testimonial.setIsFeatured(featured);
        testimonial.setUpdatedAt(LocalDateTime.now());
        Testimonial updated = testimonialRepository.save(testimonial);

        eventPublisher.publishEvent(new TestimonialDomainEvent(this, updated, "UPDATED"));
        return updated;
    }

    @Transactional
    public void deleteTestimonial(String id) {
        log.info("Deleting testimonial with id: {}", id);
        Testimonial testimonial = testimonialRepository.findById(id).orElse(null);
        if (testimonial != null) {
            testimonialRepository.deleteById(id);
            eventPublisher.publishEvent(new TestimonialDomainEvent(this, testimonial, "DELETED"));
        }
    }

    public Page<Testimonial> getFeaturedTestimonials(int page, int size) {
        return testimonialRepository.findByIsFeaturedTrue(PageRequest.of(page, size));
    }

    public Page<Testimonial> getAllTestimonials(int page, int size) {
        return testimonialRepository.findAll(PageRequest.of(page, size));
    }

    public Page<TestimonialDocument> searchTestimonialsByUsername(String username, int page, int size) {
        if (username == null || username.trim().isEmpty()) {
            return testimonialSearchRepository.findAll(PageRequest.of(page, size));
        }
        return testimonialSearchRepository.findByNameContainingIgnoreCase(username.trim(), PageRequest.of(page, size));
    }
}
