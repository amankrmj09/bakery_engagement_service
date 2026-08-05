package com.blubugtech.bakery_engagement_service.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String productId;
    private String orderId;
    private String userId;
    private String userName;
    private Integer rating;
    private String comment;
    @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    @Builder.Default private Boolean isReported = false;
    @Builder.Default private Boolean isVerifiedPurchase = false;
    @Builder.Default private Boolean isApproved = false;
    private String reportReason;
    private LocalDateTime reportedAt;
}
