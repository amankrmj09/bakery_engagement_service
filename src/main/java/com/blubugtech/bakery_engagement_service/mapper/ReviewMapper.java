package com.blubugtech.bakery_engagement_service.mapper;

import com.blubugtech.bakery_engagement_service.dto.review.ReviewRequest;
import com.blubugtech.bakery_engagement_service.dto.review.ReviewResponse;
import com.blubugtech.bakery_engagement_service.dto.review.ReviewUpdateRequest;
import com.blubugtech.bakery_engagement_service.entity.Review;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
    ReviewResponse toResponse(Review review);
    Review toEntity(ReviewRequest request);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Review review, ReviewUpdateRequest request);
}
