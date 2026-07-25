package com.blubugtech.bakery_engagement_service.search.repository;

import com.blubugtech.bakery_engagement_service.search.document.TestimonialDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialSearchRepository extends ElasticsearchRepository<TestimonialDocument, String> {
    Page<TestimonialDocument> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<TestimonialDocument> findByNameOrMessage(String name, String message, Pageable pageable);
}
