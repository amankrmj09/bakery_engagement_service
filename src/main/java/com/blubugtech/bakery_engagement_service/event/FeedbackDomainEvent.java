package com.blubugtech.bakery_engagement_service.event;

import com.blubugtech.bakery_engagement_service.entity.Feedback;
import org.springframework.context.ApplicationEvent;

public class FeedbackDomainEvent extends ApplicationEvent {
    private final Feedback feedback;
    private final String action;

    public FeedbackDomainEvent(Object source, Feedback feedback, String action) {
        super(source);
        this.feedback = feedback;
        this.action = action;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public String getAction() {
        return action;
    }
}
