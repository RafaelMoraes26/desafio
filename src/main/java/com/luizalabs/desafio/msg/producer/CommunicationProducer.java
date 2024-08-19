package com.luizalabs.desafio.msg.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void sendMessage(Communication communication) {
        log.info(">>> tentando enviar. {}", communicationQueue.getName());
        amqpTemplate.convertAndSend(communicationQueue.getName(), communication);
        log.info("Sent communication message: {}", communication);
    }
}
