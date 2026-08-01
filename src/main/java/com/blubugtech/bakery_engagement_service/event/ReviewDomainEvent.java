package com.blubugtech.bakery_engagement_service.event;

import com.blubugtech.bakery_engagement_service.entity.Review;
import org.springframework.context.ApplicationEvent;

public class ReviewDomainEvent extends ApplicationEvent {
    private final Review review;
    private final String action;

    public ReviewDomainEvent(Object source, Review review, String action) {
        super(source);
        this.review = review;
        this.action = action;
    }

    public Review getReview() {
        return review;
    }

    public String getAction() {
        return action;
    }
}
