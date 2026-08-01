package com.blubugtech.bakery_engagement_service.event;

import com.blubugtech.bakery_engagement_service.entity.Review;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReviewDomainEvent extends ApplicationEvent {
    private final Review review;
    private final String action;

    public ReviewDomainEvent(Object source, Review review, String action) {
        super(source);
        this.review = review;
        this.action = action;
    }
}
