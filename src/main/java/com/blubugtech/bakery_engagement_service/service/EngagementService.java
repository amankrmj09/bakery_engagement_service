package com.blubugtech.bakery_engagement_service.service;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import com.blubugtech.bakery_engagement_service.repository.FeedbackRepository;
import com.blubugtech.bakery_engagement_service.repository.TestimonialRepository;
import com.blubugtech.bakery_engagement_service.entity.ContactDetails;
import com.blubugtech.bakery_engagement_service.repository.ContactDetailsRepository;
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
import java.util.Map;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import com.blubugtech.common.constants.KafkaTopics;
import com.blubugtech.common.event.FeedbackEvent;
import com.blubugtech.common.contract.messaging.FeedbackPayload;

@Slf4j
@Service
@RequiredArgsConstructor
public class EngagementService {

    private final TestimonialRepository testimonialRepository;
    private final FeedbackRepository feedbackRepository;
    private final TestimonialSearchRepository testimonialSearchRepository;
    private final FeedbackSearchRepository feedbackSearchRepository;
    private final ContactDetailsRepository contactDetailsRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

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

        try {
            FeedbackPayload payload = FeedbackPayload.builder()
                .feedbackId(UUID.randomUUID()) // using a random UUID as String ID is used for mongo
                .firstName(saved.getName())
                .customerEmail(saved.getEmail())
                .type("TESTIMONIAL")
                .rating(saved.getRating())
                .timestamp(LocalDateTime.now())
                .build();
                
            FeedbackEvent event = new FeedbackEvent();
            event.setPayload(payload);
            event.setEventId(UUID.randomUUID().toString());
            event.setTimestamp(java.time.Instant.now());

            kafkaTemplate.send(KafkaTopics.FEEDBACK_TOPIC, event.getEventId(), event);
            log.info("Sent testimonial notification event for: {}", saved.getName());
        } catch (Exception e) {
            log.error("Failed to send Kafka event for testimonial: {}", e.getMessage());
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

        try {
            FeedbackPayload payload = FeedbackPayload.builder()
                .feedbackId(UUID.randomUUID())
                .firstName(saved.getName())
                .customerEmail(saved.getEmail())
                .type(saved.getType() != null ? saved.getType() : "FEEDBACK")
                .timestamp(LocalDateTime.now())
                .build();
                
            FeedbackEvent event = new FeedbackEvent();
            event.setPayload(payload);
            event.setEventId(UUID.randomUUID().toString());
            event.setTimestamp(java.time.Instant.now());

            kafkaTemplate.send(KafkaTopics.FEEDBACK_TOPIC, event.getEventId(), event);
            log.info("Sent feedback notification event for: {}", saved.getName());
        } catch (Exception e) {
            log.error("Failed to send Kafka event for feedback: {}", e.getMessage());
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

    public org.springframework.data.domain.Page<Testimonial> getFeaturedTestimonials(int page, int size) {
        return testimonialRepository.findByIsFeaturedTrue(PageRequest.of(page, size));
    }

    public org.springframework.data.domain.Page<Testimonial> getAllTestimonials(int page, int size) {
        return testimonialRepository.findAll(PageRequest.of(page, size));
    }

    public org.springframework.data.domain.Page<Feedback> getAllFeedbacks(int page, int size) {
        return feedbackRepository.findAll(PageRequest.of(page, size));
    }

    public org.springframework.data.domain.Page<TestimonialDocument> searchTestimonialsByUsername(String username, int page, int size) {
        if (username == null || username.trim().isEmpty()) {
            return testimonialSearchRepository.findAll(PageRequest.of(page, size));
        }
        return testimonialSearchRepository.findByNameContainingIgnoreCase(username.trim(), PageRequest.of(page, size));
    }

    public org.springframework.data.domain.Page<FeedbackDocument> searchFeedbacksByUsername(String query, String type, int page, int size) {
        if (query == null || query.trim().isEmpty()) {
            if (type != null && !type.trim().isEmpty()) {
                return feedbackSearchRepository.findByType(type.trim(), PageRequest.of(page, size));
            }
            return feedbackSearchRepository.findAll(PageRequest.of(page, size));
        }
        
        String q = query.trim();
        if (type != null && !type.trim().isEmpty()) {
            String t = type.trim();
            return feedbackSearchRepository.findByTypeAndNameContainingIgnoreCaseOrTypeAndEmailContainingIgnoreCase(
                t, q, t, q, PageRequest.of(page, size)
            );
        }
        return feedbackSearchRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q, PageRequest.of(page, size));
    }

    public ContactDetails getContactDetails() {
        List<ContactDetails> details = contactDetailsRepository.findAll();
        if (details.isEmpty()) {
            Map<String, String> defaultSocialLinks = new java.util.HashMap<>();
            defaultSocialLinks.put("instagram", "");
            defaultSocialLinks.put("facebook", "");
            defaultSocialLinks.put("twitter", "");
            defaultSocialLinks.put("threads", "");
            defaultSocialLinks.put("website", "");
            ContactDetails defaultDetails = ContactDetails.builder()
                    .address("")
                    .phoneNumbers(List.of())
                    .emails(List.of())
                    .socialLinks(defaultSocialLinks)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            return contactDetailsRepository.save(defaultDetails);
        }
        ContactDetails existing = details.get(0);
        // Backfill socialLinks for existing records that predate this field
        if (existing.getSocialLinks() == null) {
            Map<String, String> emptyLinks = new java.util.HashMap<>();
            emptyLinks.put("instagram", "");
            emptyLinks.put("facebook", "");
            emptyLinks.put("twitter", "");
            emptyLinks.put("threads", "");
            emptyLinks.put("website", "");
            existing.setSocialLinks(emptyLinks);
            contactDetailsRepository.save(existing);
        } else if (!existing.getSocialLinks().containsKey("website")) {
            existing.getSocialLinks().put("website", "");
            contactDetailsRepository.save(existing);
        }
        return existing;
    }

    @Transactional
    public ContactDetails updateContactDetails(ContactDetails request) {
        ContactDetails current = getContactDetails();
        current.setAddress(request.getAddress());
        current.setPhoneNumbers(request.getPhoneNumbers());
        current.setEmails(request.getEmails());
        if (request.getSocialLinks() != null) {
            current.setSocialLinks(request.getSocialLinks());
        }
        current.setUpdatedAt(LocalDateTime.now());
        return contactDetailsRepository.save(current);
    }
}
