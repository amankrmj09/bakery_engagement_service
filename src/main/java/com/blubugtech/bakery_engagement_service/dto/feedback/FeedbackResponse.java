package com.blubugtech.bakery_engagement_service.dto.feedback;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FeedbackResponse {
    private String id;
    private String type;
    private String message;
    private String contactEmail;
    private String name;
    @Builder.Default private String status = "NEW";
    @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
}
