package com.luizalabs.desafio.service;

import java.time.*;

import org.springframework.stereotype.Service;

import com.luizalabs.desafio.dto.request.CommunicationRequest;
import com.luizalabs.desafio.dto.response.CommunicationResponse;
import com.luizalabs.desafio.enums.CommunicationSendStatus;
import com.luizalabs.desafio.exception.CommunicationAlreadyCancelledException;
import com.luizalabs.desafio.exception.CommunicationNotFoundException;
import com.luizalabs.desafio.exception.CommunicationScheduleTimeExpiredException;
import com.luizalabs.desafio.mapper.CommunicationMapper;
import com.luizalabs.desafio.msg.CommunicationProducer;
import com.luizalabs.desafio.repository.CommunicationRepository;
import com.luizalabs.desafio.repository.entity.Communication;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommunicationService {

    private final CommunicationRepository repository;
    private final CommunicationMapper mapper;
    private final CommunicationProducer producer;

    public CommunicationService(CommunicationRepository repository, CommunicationMapper mapper, CommunicationProducer producer) {
        this.repository = repository;
        this.mapper = mapper;
        this.producer = producer;
    }

    public CommunicationResponse create(CommunicationRequest request) {
        validateCommunicationScheduleTime(request.getScheduledTime());
        Communication entity = mapper.fromRequestToEntity(request);
        entity = repository.save(entity);
        return mapper.fromEntityToResponse(entity);
    }

    public CommunicationResponse getScheduleStatusById(Long id) {
        return mapper.fromEntityToResponse(findById(id));
    }

    public CommunicationResponse cancelSendById(Long id) {
        Communication communication = findById(id);
        validateCommunicationStatusIsScheduled(communication);
        return mapper.fromEntityToResponse(updateCommunicationStatus(communication.getId(), CommunicationSendStatus.CANCELLED));
    }

    public void sendCommunication(Long id) {
        // TODO: Aqui deve ser implementada a lógica de envio agendado ao rabbitMQ.
        Communication communication = findById(id);
        validateCommunicationStatusIsScheduled(communication);
        if(producer.sendMessage(communication)) {
            updateCommunicationStatus(communication.getId(), CommunicationSendStatus.SENT);
        }
    }

    private Communication findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new CommunicationNotFoundException("Communication with id [" + id + "] not found."));
    }

    private Communication updateCommunicationStatus(Long id, CommunicationSendStatus status) {
        Communication communication = findById(id);
        communication.setStatus(status);
        return repository.save(communication);
    }

    private void validateCommunicationStatusIsScheduled(Communication communication) {
        if(!communication.getStatus().equals(CommunicationSendStatus.SCHEDULED))
            throw new CommunicationAlreadyCancelledException("Communication with id [" + communication.getId() + "] is invalid.");
    }

    private void validateCommunicationScheduleTime(@NotNull LocalDateTime scheduleTime) {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        ZonedDateTime zonedScheduledTime = scheduleTime.atZone(zoneId);
        OffsetDateTime scheduledOffsetTime = zonedScheduledTime.toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC);
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        if (scheduledOffsetTime.isBefore(now)) {
            throw new CommunicationScheduleTimeExpiredException("ScheduleTime of communication is expired. Set a future time.");
        }
    }
}
