package com.luizalabs.desafio.msg;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

import com.luizalabs.desafio.repository.entity.Communication;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommunicationProducer {

    private final AmqpTemplate amqpTemplate;
    private final Queue communicationQueue;


    public CommunicationProducer(AmqpTemplate amqpTemplate, Queue communicationQueue) {
        this.amqpTemplate = amqpTemplate;
        this.communicationQueue = communicationQueue;
    }

    public boolean sendMessage(Communication communication) {
        try {
            log.info("Sending communication message: {}", communication);
            amqpTemplate.convertAndSend(communicationQueue.getName(), communication);
            return true;
        } catch (Exception e) {
            log.error("Failed to send communication message: {}", communication, e);
            return false;
        }
    }
}
