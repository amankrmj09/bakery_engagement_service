package com.blubugtech.bakery_engagement_service.event;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FeedbackDomainEvent extends ApplicationEvent {
    private final Feedback feedback;
    private final String action;

    public FeedbackDomainEvent(Object source, Feedback feedback, String action) {
        super(source);
        this.feedback = feedback;
        this.action = action;
    }
}
