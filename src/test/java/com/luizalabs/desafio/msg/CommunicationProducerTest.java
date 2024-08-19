package com.luizalabs.desafio.msg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;

import static com.luizalabs.desafio.utils.DTOUtils.getDefaultEntity;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.luizalabs.desafio.repository.entity.Communication;

@ExtendWith(MockitoExtension.class)
class CommunicationProducerTest {

    @Mock
    private AmqpTemplate amqpTemplate;

    @Mock
    private Queue communicationQueue;

    @InjectMocks
    private CommunicationProducer producer;

    private final Communication communication = getDefaultEntity();

    @Test
    void sendMessage_success() {
        when(communicationQueue.getName()).thenReturn("communicationQueue");

        boolean result = producer.sendMessage(communication);

        assertTrue(result);
        verify(amqpTemplate, times(1)).convertAndSend("communicationQueue", communication);
    }

    @Test
    void sendMessage_failure() {
        when(communicationQueue.getName()).thenReturn("communicationQueue");
        doThrow(new RuntimeException("Error sending message")).when(amqpTemplate).convertAndSend(anyString(), any(Communication.class));

        boolean result = producer.sendMessage(communication);

        assertFalse(result);
        verify(amqpTemplate, times(1)).convertAndSend("communicationQueue", communication);
    }
}

