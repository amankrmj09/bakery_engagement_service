package com.blubugtech.bakery_engagement_service.dto.testimonial;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TestimonialResponse {
    private String id;
    private String authorName;
    private String content;
    private Integer rating;
    private String role;
    private String avatarUrl;
    @Builder.Default private boolean isApproved = false;
    @Builder.Default private boolean isFeatured = false;
    @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
}
