package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.dto.contact.ContactDetailsRequest;
import com.blubugtech.bakery_engagement_service.dto.contact.ContactDetailsResponse;
import com.blubugtech.bakery_engagement_service.entity.ContactDetails;
import com.blubugtech.bakery_engagement_service.mapper.ContactDetailsMapper;
import com.blubugtech.bakery_engagement_service.service.ContactDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/engagement/contact-details")
@RequiredArgsConstructor
@Tag(name = "Contact Details", description = "Contact Details management APIs")
@Slf4j
public class ContactDetailsController {

    private final ContactDetailsService contactDetailsService;
    private final ContactDetailsMapper contactDetailsMapper;

    @Operation(summary = "Get contact details")
    @GetMapping
    public ResponseEntity<ContactDetailsResponse> getContactDetails() {
        return ResponseEntity.ok(contactDetailsMapper.toResponse(contactDetailsService.getContactDetails()));
    }

    @Operation(summary = "Update contact details")
    @PutMapping
    public ResponseEntity<ContactDetailsResponse> updateContactDetails(@Valid @RequestBody ContactDetailsRequest request) {
        ContactDetails saved = contactDetailsService.updateContactDetails(contactDetailsMapper.toEntity(request));
        return ResponseEntity.ok(contactDetailsMapper.toResponse(saved));
    }
}
