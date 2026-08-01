package com.blubugtech.bakery_engagement_service.event;

import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import org.springframework.context.ApplicationEvent;

public class TestimonialDomainEvent extends ApplicationEvent {
    private final Testimonial testimonial;
    private final String action;

    public TestimonialDomainEvent(Object source, Testimonial testimonial, String action) {
        super(source);
        this.testimonial = testimonial;
        this.action = action;
    }

    public Testimonial getTestimonial() {
        return testimonial;
    }

    public String getAction() {
        return action;
    }
}
