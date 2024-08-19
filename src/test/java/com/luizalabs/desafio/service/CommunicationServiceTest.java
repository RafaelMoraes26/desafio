package com.luizalabs.desafio.service;

import static com.luizalabs.desafio.utils.DTOUtils.getDefaultEntity;
import static com.luizalabs.desafio.utils.DTOUtils.getDefaultRequest;
import static com.luizalabs.desafio.utils.DTOUtils.getDefaultResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.luizalabs.desafio.dto.request.CommunicationRequest;
import com.luizalabs.desafio.dto.response.CommunicationResponse;
import com.luizalabs.desafio.enums.CommunicationSendStatus;
import com.luizalabs.desafio.enums.CommunicationType;
import com.luizalabs.desafio.exception.CommunicationAlreadyCancelledException;
import com.luizalabs.desafio.exception.CommunicationNotFoundException;
import com.luizalabs.desafio.mapper.CommunicationMapper;
import com.luizalabs.desafio.msg.CommunicationProducer;
import com.luizalabs.desafio.repository.CommunicationRepository;
import com.luizalabs.desafio.repository.entity.Communication;

@ExtendWith(MockitoExtension.class)
public class CommunicationServiceTest {

    @Mock
    private CommunicationRepository repository;

    @Mock
    private CommunicationMapper mapper;

    @Mock
    private CommunicationProducer producer;

    @InjectMocks
    private CommunicationService service;

    @Captor
    private ArgumentCaptor<Communication> communicationCaptor;

    private final Communication entity = getDefaultEntity();
    private final CommunicationRequest request = getDefaultRequest();
    private final CommunicationResponse response = getDefaultResponse();


    @Test
    void createCommunication_success() {
        when(mapper.fromRequestToEntity(any(CommunicationRequest.class))).thenCallRealMethod();
        when(repository.save(any(Communication.class))).thenReturn(entity);
        when(mapper.fromEntityToResponse(any(Communication.class))).thenCallRealMethod();

        CommunicationResponse result = service.create(request);

        assertNotNull(result);
        assertEquals(response.getMessage(), result.getMessage());
        assertEquals(response.getStatus(), result.getStatus());

        verify(repository, times(1)).save(communicationCaptor.capture());
        Communication savedEntity = communicationCaptor.getValue();

        assertEquals(result.getMessage(), savedEntity.getMessage());
        assertEquals(result.getReceiver(), savedEntity.getReceiver());
        assertEquals(CommunicationType.PUSH, savedEntity.getType());
        assertEquals(CommunicationSendStatus.SCHEDULED, savedEntity.getStatus());
    }

    @Test
    void getScheduleStatusById_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.fromEntityToResponse(any(Communication.class))).thenReturn(response);

        CommunicationResponse result = service.getScheduleStatusById(1L);

        assertNotNull(result);
        assertEquals(response.getMessage(), result.getMessage());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void cancelSendById_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any(Communication.class))).thenReturn(entity);
        when(mapper.fromEntityToResponse(any(Communication.class))).thenReturn(response);

        CommunicationResponse result = service.cancelSendById(1L);

        assertNotNull(result);
        assertEquals(CommunicationSendStatus.CANCELLED, entity.getStatus());
        verify(repository, times(1)).save(entity);
    }

    @Test
    void cancelSendById_alreadyCancelled() {
        entity.setStatus(CommunicationSendStatus.CANCELLED);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        assertThrows(CommunicationAlreadyCancelledException.class, () -> service.cancelSendById(1L));
    }

    @Test
    void sendCommunication_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(producer.sendMessage(entity)).thenReturn(true);

        service.sendCommunication(1L);

        verify(producer, times(1)).sendMessage(entity);
        verify(repository, times(1)).save(entity);
        assertEquals(CommunicationSendStatus.SENT, entity.getStatus());
    }

    @Test
    void sendCommunication_failureToSend() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(producer.sendMessage(entity)).thenReturn(false);

        service.sendCommunication(1L);

        verify(producer, times(1)).sendMessage(entity);
        verify(repository, never()).save(entity);
        assertEquals(CommunicationSendStatus.SCHEDULED, entity.getStatus());
    }

    @Test
    void sendCommunication_alreadyCancelled() {
        entity.setStatus(CommunicationSendStatus.CANCELLED);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        assertThrows(CommunicationAlreadyCancelledException.class, () -> service.sendCommunication(1L));
    }

    @Test
    void sendCommunication_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CommunicationNotFoundException.class, () -> service.sendCommunication(1L));

        verify(producer, never()).sendMessage(any(Communication.class));  // Verifica que sendMessage não foi chamado
    }

    @Test
    void findById_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CommunicationNotFoundException.class, () -> service.getScheduleStatusById(1L));
    }
}