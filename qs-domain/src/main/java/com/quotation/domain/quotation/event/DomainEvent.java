package com.quotation.domain.quotation.event;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 领域事件基类
 */
@Data
public abstract class DomainEvent {
    private String eventId;
    private LocalDateTime occurredOn;

    public DomainEvent() {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
    }
}
