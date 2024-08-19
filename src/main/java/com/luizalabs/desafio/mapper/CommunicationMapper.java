package com.luizalabs.desafio.mapper;

import java.time.*;

import org.springframework.stereotype.Component;

import com.luizalabs.desafio.dto.request.CommunicationRequest;
import com.luizalabs.desafio.dto.response.CommunicationResponse;
import com.luizalabs.desafio.enums.CommunicationSendStatus;
import com.luizalabs.desafio.repository.entity.Communication;

@Component
public class CommunicationMapper {

    public CommunicationResponse fromEntityToResponse(Communication entity) {
        CommunicationResponse response = new CommunicationResponse();
        response.setId(entity.getId());
        response.setMessage(entity.getMessage());
        response.setReceiver(entity.getReceiver());
        response.setScheduledTime(sanitizeZoneToResponse(entity.getScheduledTime()));
        response.setType(entity.getType());
        response.setStatus(entity.getStatus());
        return response;
    }

    public Communication fromRequestToEntity(CommunicationRequest request) {
        Communication entity = new Communication();
        entity.setMessage(request.getMessage());
        entity.setReceiver(request.getReceiver());
        entity.setScheduledTime(sanitizeZoneFromRequest(request.getScheduledTime()));
        entity.setType(request.getType());
        entity.setStatus(CommunicationSendStatus.SCHEDULED);
        return entity;
    }

    private OffsetDateTime sanitizeZoneFromRequest(LocalDateTime ldt) {
        return ldt.atZone(ZoneId.systemDefault())
            .withZoneSameInstant(ZoneOffset.UTC)
            .toOffsetDateTime();
    }

    private LocalDateTime sanitizeZoneToResponse(OffsetDateTime odt) {
        return odt.atZoneSameInstant(ZoneId.of("America/Sao_Paulo"))
            .toLocalDateTime();
    }
}
