package com.blubugtech.bakery_engagement_service.search.repository;

import com.blubugtech.bakery_engagement_service.search.document.FeedbackDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackSearchRepository extends ElasticsearchRepository<FeedbackDocument, String> {
    Page<FeedbackDocument> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);
}
