package com.blubugtech.bakery_engagement_service.repository;

import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestimonialRepository extends MongoRepository<Testimonial, String> {
    List<Testimonial> findByIsFeaturedTrue();
    Page<Testimonial> findByIsFeaturedTrue(Pageable pageable);
    Page<Testimonial> findByStatus(String status, Pageable pageable);
}
