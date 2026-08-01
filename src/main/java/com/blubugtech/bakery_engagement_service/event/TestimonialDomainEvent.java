package com.blubugtech.bakery_engagement_service.event;

import com.blubugtech.bakery_engagement_service.entity.Testimonial;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TestimonialDomainEvent extends ApplicationEvent {
    private final Testimonial testimonial;
    private final String action;

    public TestimonialDomainEvent(Object source, Testimonial testimonial, String action) {
        super(source);
        this.testimonial = testimonial;
        this.action = action;
    }
}
