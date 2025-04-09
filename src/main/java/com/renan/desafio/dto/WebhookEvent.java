package com.renan.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebhookEvent {
    private long eventId;
    private long subscriptionId;
    private long portalId;
    private long appId;
    private long occurredAt;
    private String subscriptionType;
    private int attemptNumber;
    private long objectId;
    private String changeFlag;
    private String changeSource;
    private String sourceId;
}
