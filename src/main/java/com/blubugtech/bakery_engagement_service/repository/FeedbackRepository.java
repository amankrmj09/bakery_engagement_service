package com.blubugtech.bakery_engagement_service.repository;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    List<Feedback> findByStatus(String status);
    List<Feedback> findByType(String type);
}
