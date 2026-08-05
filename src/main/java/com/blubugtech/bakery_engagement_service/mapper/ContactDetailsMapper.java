package com.blubugtech.bakery_engagement_service.mapper;

import com.blubugtech.bakery_engagement_service.dto.contact.ContactDetailsRequest;
import com.blubugtech.bakery_engagement_service.dto.contact.ContactDetailsResponse;
import com.blubugtech.bakery_engagement_service.entity.ContactDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactDetailsMapper {
    ContactDetailsResponse toResponse(ContactDetails contactDetails);
    ContactDetails toEntity(ContactDetailsRequest request);
}
