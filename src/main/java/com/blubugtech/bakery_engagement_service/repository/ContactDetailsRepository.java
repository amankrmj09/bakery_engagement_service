package com.blubugtech.bakery_engagement_service.repository;

import com.blubugtech.bakery_engagement_service.entity.ContactDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactDetailsRepository extends MongoRepository<ContactDetails, String> {
}
