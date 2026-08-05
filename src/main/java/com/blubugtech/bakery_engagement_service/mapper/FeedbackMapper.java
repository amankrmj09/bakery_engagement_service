package com.blubugtech.bakery_engagement_service.mapper;

import com.blubugtech.bakery_engagement_service.dto.feedback.FeedbackRequest;
import com.blubugtech.bakery_engagement_service.dto.feedback.FeedbackResponse;
import com.blubugtech.bakery_engagement_service.entity.Feedback;
import com.blubugtech.bakery_engagement_service.search.document.FeedbackDocument;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedbackMapper {

    @Mapping(target = "contactEmail", source = "email")
    FeedbackResponse toResponse(Feedback feedback);

    @Mapping(target = "email", source = "contactEmail")
    Feedback toEntity(FeedbackRequest request);

    @Mapping(target = "contactEmail", source = "email")
    FeedbackResponse toResponse(FeedbackDocument doc);
}
