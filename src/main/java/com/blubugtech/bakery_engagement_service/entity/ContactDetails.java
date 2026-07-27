package com.blubugtech.bakery_engagement_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contact_details")
public class ContactDetails {

    @Id
    private String id;

    private String address;
    private List<String> phoneNumbers;
    private List<String> emails;

    /**
     * Social media profile URLs.
     * Keys: instagram, facebook, twitter, threads
     */
    private Map<String, String> socialLinks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
