package com.blubugtech.bakery_engagement_service.search.repository;

import com.blubugtech.bakery_engagement_service.search.document.FeedbackDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackSearchRepository extends ElasticsearchRepository<FeedbackDocument, String> {
    Page<FeedbackDocument> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);
    
    Page<FeedbackDocument> findByType(String type, Pageable pageable);
    
    // In Spring Data Elasticsearch, to do (type = ? AND name LIKE ?) OR (type = ? AND email LIKE ?) 
    // it's better to use `@Query` because the method name parser can get confused with OR and AND precedence.
    // However, Spring Data usually binds AND tighter than OR: TypeAndName Or TypeAndEmail
    Page<FeedbackDocument> findByTypeAndNameContainingIgnoreCaseOrTypeAndEmailContainingIgnoreCase(
        String type1, String name, String type2, String email, Pageable pageable);
}
