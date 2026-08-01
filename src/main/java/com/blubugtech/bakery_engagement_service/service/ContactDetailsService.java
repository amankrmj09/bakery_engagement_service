package com.blubugtech.bakery_engagement_service.service;

import com.blubugtech.bakery_engagement_service.entity.ContactDetails;
import com.blubugtech.bakery_engagement_service.repository.ContactDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactDetailsService {

    private final ContactDetailsRepository contactDetailsRepository;

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
