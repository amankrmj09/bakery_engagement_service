package com.blubugtech.bakery_engagement_service.dto.testimonial;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class TestimonialRequest {
    @NotBlank(message = "Author name is required")
    @Size(max = 100)
    private String authorName;

    @NotBlank(message = "Content is required")
    @Size(max = 1000)
    private String content;

    @Min(1) @Max(5)
    private Integer rating;

    @Size(max = 100)
    private String role;

    @Size(max = 255)
    private String avatarUrl;
}
