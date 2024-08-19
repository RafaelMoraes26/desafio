package com.luizalabs.desafio.utils;

import java.time.OffsetDateTime;

import com.luizalabs.desafio.dto.request.CommunicationRequest;
import com.luizalabs.desafio.dto.response.CommunicationResponse;
import com.luizalabs.desafio.enums.CommunicationSendStatus;
import com.luizalabs.desafio.enums.CommunicationType;
import com.luizalabs.desafio.repository.entity.Communication;

public class DTOUtils {

    private static final String MESSAGE = "Test message";
    private static final String RECEIVER = "Test receiver";
    private static final OffsetDateTime SCHEDULE_TIME = OffsetDateTime.now().plusDays(10);
    private static final OffsetDateTime NOW = OffsetDateTime.now();

    public static CommunicationResponse getDefaultResponse() {
        CommunicationResponse response = new CommunicationResponse();
        response.setId(1L);
        response.setMessage(MESSAGE);
        response.setReceiver(RECEIVER);
        response.setScheduledTime(SCHEDULE_TIME.toLocalDateTime());
        response.setType(CommunicationType.PUSH);
        response.setStatus(CommunicationSendStatus.SCHEDULED);
        return response;
    }

    public static CommunicationRequest getDefaultRequest() {
        CommunicationRequest request = new CommunicationRequest();
        request.setMessage(MESSAGE);
        request.setReceiver(RECEIVER);
        request.setScheduledTime(SCHEDULE_TIME.toLocalDateTime());
        request.setType(CommunicationType.PUSH);
        return request;
    }

    public static Communication getDefaultEntity() {
        Communication entity = new Communication();
        entity.setId(1L);
        entity.setMessage(MESSAGE);
        entity.setReceiver(RECEIVER);
        entity.setScheduledTime(SCHEDULE_TIME);
        entity.setType(CommunicationType.PUSH);
        entity.setStatus(CommunicationSendStatus.SCHEDULED);
        entity.setCreatedAt(NOW);
        entity.setUpdatedAt(NOW);
        return entity;
    }
}
