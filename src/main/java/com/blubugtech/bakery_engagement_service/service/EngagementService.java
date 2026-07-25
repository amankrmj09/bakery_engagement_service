package com.blubugtech.bakery_engagement_service.service;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import com.blubugtech.bakery_engagement_service.repository.FeedbackRepository;
import com.blubugtech.bakery_engagement_service.repository.TestimonialRepository;
import com.blubugtech.bakery_engagement_service.search.document.FeedbackDocument;
import com.blubugtech.bakery_engagement_service.search.document.TestimonialDocument;
import com.blubugtech.bakery_engagement_service.search.repository.FeedbackSearchRepository;
import com.blubugtech.bakery_engagement_service.search.repository.TestimonialSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EngagementService {

    private final TestimonialRepository testimonialRepository;
    private final FeedbackRepository feedbackRepository;
    private final TestimonialSearchRepository testimonialSearchRepository;
    private final FeedbackSearchRepository feedbackSearchRepository;

    @Transactional
    public Testimonial createTestimonial(Testimonial testimonial) {
        log.info("Creating new testimonial for user: {}", testimonial.getName());
        if (testimonial.getCreatedAt() == null) {
            testimonial.setCreatedAt(LocalDateTime.now());
        }
        testimonial.setUpdatedAt(LocalDateTime.now());
        Testimonial saved = testimonialRepository.save(testimonial);
        
        try {
            TestimonialDocument doc = TestimonialDocument.builder()
                    .id(saved.getId())
                    .uid(saved.getUid())
                    .name(saved.getName())
                    .profileImageUrl(saved.getProfileImageUrl())
                    .rating(saved.getRating())
                    .message(saved.getMessage())
                    .isFeatured(saved.getIsFeatured())
                    .status(saved.getStatus())
                    .build();
            testimonialSearchRepository.save(doc);
            log.debug("Indexed testimonial {} into Elasticsearch", saved.getId());
        } catch (Exception e) {
            log.error("Failed to index testimonial into Elasticsearch: {}", e.getMessage());
        }
        
        return saved;
    }

    @Transactional
    public Feedback createFeedback(Feedback feedback) {
        log.info("Creating new feedback from: {}", feedback.getName());
        if (feedback.getCreatedAt() == null) {
            feedback.setCreatedAt(LocalDateTime.now());
        }
        feedback.setUpdatedAt(LocalDateTime.now());
        Feedback saved = feedbackRepository.save(feedback);
        
        try {
            FeedbackDocument doc = FeedbackDocument.builder()
                    .id(saved.getId())
                    .uid(saved.getUid())
                    .name(saved.getName())
                    .email(saved.getEmail())
                    .type(saved.getType())
                    .message(saved.getMessage())
                    .status(saved.getStatus())
                    .build();
            feedbackSearchRepository.save(doc);
            log.debug("Indexed feedback {} into Elasticsearch", saved.getId());
        } catch (Exception e) {
            log.error("Failed to index feedback into Elasticsearch: {}", e.getMessage());
        }
        
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

        try {
            testimonialSearchRepository.findById(id).ifPresent(doc -> {
                doc.setIsFeatured(featured);
                testimonialSearchRepository.save(doc);
            });
        } catch (Exception e) {
            log.warn("Could not update featured status in Elasticsearch for id {}: {}", id, e.getMessage());
        }

        return updated;
    }

    public List<Testimonial> getFeaturedTestimonials() {
        return testimonialRepository.findByIsFeaturedTrue();
    }

    public List<Testimonial> getAllTestimonials() {
        return testimonialRepository.findAll();
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public List<TestimonialDocument> searchTestimonialsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return testimonialSearchRepository.findAll(PageRequest.of(0, 50)).getContent();
        }
        return testimonialSearchRepository.findByNameContainingIgnoreCase(username.trim(), PageRequest.of(0, 50)).getContent();
    }

    public List<FeedbackDocument> searchFeedbacksByUsername(String query) {
        if (query == null || query.trim().isEmpty()) {
            return feedbackSearchRepository.findAll(PageRequest.of(0, 50)).getContent();
        }
        return feedbackSearchRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query.trim(), query.trim(), PageRequest.of(0, 50)).getContent();
    }
}
