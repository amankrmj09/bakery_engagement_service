package com.blubugtech.bakery_engagement_service.dto.feedback;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class FeedbackRequest {
    @NotBlank(message = "Feedback type is required")
    @Size(max = 50)
    private String type;

    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message must not exceed 2000 characters")
    private String message;

    @Email(message = "Invalid email format")
    @Size(max = 255)
    private String contactEmail;

    @Size(max = 100)
    private String name;
}
