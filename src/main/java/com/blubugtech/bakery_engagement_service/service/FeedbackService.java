package com.blubugtech.bakery_engagement_service.service;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import com.blubugtech.bakery_engagement_service.event.FeedbackDomainEvent;
import com.blubugtech.bakery_engagement_service.repository.FeedbackRepository;
import com.blubugtech.bakery_engagement_service.search.document.FeedbackDocument;
import com.blubugtech.bakery_engagement_service.search.repository.FeedbackSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackSearchRepository feedbackSearchRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Feedback createFeedback(Feedback feedback) {
        log.info("Creating new feedback from: {}", feedback.getName());
        if (feedback.getCreatedAt() == null) {
            feedback.setCreatedAt(LocalDateTime.now());
        }
        feedback.setUpdatedAt(LocalDateTime.now());
        Feedback saved = feedbackRepository.save(feedback);
        
        eventPublisher.publishEvent(new FeedbackDomainEvent(this, saved, "CREATED"));
        
        return saved;
    }

    @Transactional
    public Feedback updateFeedbackStatus(String id, String status) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found: " + id));
        feedback.setStatus(status);
        feedback.setUpdatedAt(LocalDateTime.now());
        Feedback updated = feedbackRepository.save(feedback);
        
        eventPublisher.publishEvent(new FeedbackDomainEvent(this, updated, "STATUS_UPDATED"));
        
        return updated;
    }

    @Transactional
    public void deleteFeedback(String id) {
        log.info("Deleting feedback with id: {}", id);
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        if (feedback != null) {
            feedbackRepository.deleteById(id);
            eventPublisher.publishEvent(new FeedbackDomainEvent(this, feedback, "DELETED"));
        }
    }

    public org.springframework.data.web.PagedModel<Feedback> getAllFeedbacks(org.springframework.data.domain.Pageable pageable) {
        return new org.springframework.data.web.PagedModel<>(feedbackRepository.findAll(pageable));
    }

    public org.springframework.data.web.PagedModel<FeedbackDocument> searchFeedbacksByUsername(String query, String type, org.springframework.data.domain.Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            if (type != null && !type.trim().isEmpty()) {
                return new org.springframework.data.web.PagedModel<>(feedbackSearchRepository.findByType(type.trim(), pageable));
            }
            return new org.springframework.data.web.PagedModel<>(feedbackSearchRepository.findAll(pageable));
        }
        
        String q = query.trim();
        if (type != null && !type.trim().isEmpty()) {
            String t = type.trim();
            return new org.springframework.data.web.PagedModel<>(feedbackSearchRepository.findByTypeAndNameContainingIgnoreCaseOrTypeAndEmailContainingIgnoreCase(
                t, q, t, q, pageable
            ));
        }
        return new org.springframework.data.web.PagedModel<>(feedbackSearchRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q, pageable));
    }
}
