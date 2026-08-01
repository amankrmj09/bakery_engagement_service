package com.blubugtech.bakery_engagement_service.controller;

import com.blubugtech.bakery_engagement_service.entity.ContactDetails;
import com.blubugtech.bakery_engagement_service.service.ContactDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/engagement/contact-details")
@RequiredArgsConstructor
@Tag(name = "Contact Details", description = "Contact Details management APIs")
@Slf4j
public class ContactDetailsController {

    private final ContactDetailsService contactDetailsService;

    @Operation(summary = "Get contact details")
    @GetMapping
    public ResponseEntity<ContactDetails> getContactDetails() {
        return ResponseEntity.ok(contactDetailsService.getContactDetails());
    }

    @Operation(summary = "Update contact details")
    @PutMapping
    public ResponseEntity<ContactDetails> updateContactDetails(@RequestBody ContactDetails request) {
        return ResponseEntity.ok(contactDetailsService.updateContactDetails(request));
    }
}
