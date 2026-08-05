package com.blubugtech.bakery_engagement_service.dto.contact;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ContactDetailsResponse {
    private String id;
    private String address;
    @Builder.Default private List<String> phoneNumbers = new ArrayList<>();
    @Builder.Default private List<String> emails = new ArrayList<>();
    @Builder.Default private Map<String, String> socialLinks = new HashMap<>();
    @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
