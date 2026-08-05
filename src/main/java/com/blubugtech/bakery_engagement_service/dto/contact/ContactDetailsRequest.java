package com.blubugtech.bakery_engagement_service.dto.contact;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor
public class ContactDetailsRequest {
    @NotBlank(message = "Address is required")
    private String address;

    private List<String> phoneNumbers;

    private List<String> emails;

    private Map<String, String> socialLinks;
}
