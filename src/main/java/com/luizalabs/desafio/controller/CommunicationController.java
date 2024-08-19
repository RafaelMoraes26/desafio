package com.luizalabs.desafio.controller;

import org.springframework.web.bind.annotation.*;

import com.luizalabs.desafio.dto.request.CommunicationRequest;
import com.luizalabs.desafio.dto.response.CommunicationResponse;
import com.luizalabs.desafio.exception.CommunicationNotFoundException;
import com.luizalabs.desafio.service.CommunicationService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("communication")
@Slf4j
class CommunicationController {

    private final CommunicationService service;


    CommunicationController(CommunicationService service) {
        this.service = service;
    }

    @PostMapping("/")
    public CommunicationResponse createCommunication(@Valid @RequestBody CommunicationRequest request) {
        log.info(">>> createCommunication");
        return service.create(request);
    }

    @GetMapping("/scheduleStatus/{id}")
    public CommunicationResponse getScheduleStatus(@PathVariable Long id) throws CommunicationNotFoundException {
        log.info(">>> getScheduleStatus");
        return service.getScheduleStatusById(id);
    }

    @GetMapping("/cancelSend/{id}")
    public CommunicationResponse cancelSend(@PathVariable Long id) throws CommunicationNotFoundException {
        log.info(">>> cancelSend");
        return service.cancelSendById(id);
    }

    @GetMapping("/sendToQueue/{id}")
    public void sendCommunication(@PathVariable Long id) throws CommunicationNotFoundException {
        log.info(">>> sendCommunication");
        service.sendCommunication(id);
    }
}
