package com.blubugtech.bakery_engagement_service.repository;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    Page<Feedback> findByStatus(String status, Pageable pageable);
    Page<Feedback> findByType(String type, Pageable pageable);
}
