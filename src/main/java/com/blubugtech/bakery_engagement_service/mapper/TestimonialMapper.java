package com.blubugtech.bakery_engagement_service.mapper;

import com.blubugtech.bakery_engagement_service.dto.testimonial.TestimonialRequest;
import com.blubugtech.bakery_engagement_service.dto.testimonial.TestimonialResponse;
import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import com.blubugtech.bakery_engagement_service.search.document.TestimonialDocument;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestimonialMapper {

    @Mapping(target = "authorName", source = "name")
    @Mapping(target = "content", source = "message")
    @Mapping(target = "avatarUrl", source = "profileImageUrl")
    @Mapping(target = "isApproved", expression = "java(\"APPROVED\".equalsIgnoreCase(testimonial.getStatus()))")
    TestimonialResponse toResponse(Testimonial testimonial);

    @Mapping(target = "name", source = "authorName")
    @Mapping(target = "message", source = "content")
    @Mapping(target = "profileImageUrl", source = "avatarUrl")
    Testimonial toEntity(TestimonialRequest request);

    @Mapping(target = "authorName", source = "name")
    @Mapping(target = "content", source = "message")
    @Mapping(target = "avatarUrl", source = "profileImageUrl")
    @Mapping(target = "isApproved", expression = "java(\"APPROVED\".equalsIgnoreCase(doc.getStatus()))")
    TestimonialResponse toResponse(TestimonialDocument doc);
}
