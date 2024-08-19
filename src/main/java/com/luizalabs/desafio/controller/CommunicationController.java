package com.luizalabs.desafio.controller;

import org.springframework.web.bind.annotation.*;

import com.luizalabs.desafio.dto.request.CommunicationRequest;
import com.luizalabs.desafio.dto.response.CommunicationResponse;
import com.luizalabs.desafio.exception.CommunicationNotFoundException;
import com.luizalabs.desafio.service.CommunicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new communication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Communication created successfully",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CommunicationResponse.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/")
    public CommunicationResponse createCommunication(
        @Parameter(description = "Communication details to be created", required = true)
        @Valid @RequestBody CommunicationRequest request) {
        log.info(">>> createCommunication");
        return service.create(request);
    }

    @Operation(summary = "Get the schedule status of a communication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the communication",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CommunicationResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "Communication not found", content = @Content)
    })
    @GetMapping("/scheduleStatus/{id}")
    public CommunicationResponse getScheduleStatus(
        @Parameter(description = "ID of the communication to be retrieved", required = true)
        @PathVariable Long id) throws CommunicationNotFoundException {
        log.info(">>> getScheduleStatus");
        return service.getScheduleStatusById(id);
    }

    @Operation(summary = "Cancel the sending of a communication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Communication cancelled successfully",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CommunicationResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "Communication not found", content = @Content)
    })
    @GetMapping("/cancelSend/{id}")
    public CommunicationResponse cancelSend(
        @Parameter(description = "ID of the communication to be cancelled", required = true)
        @PathVariable Long id) throws CommunicationNotFoundException {
        log.info(">>> cancelSend");
        return service.cancelSendById(id);
    }

    @Operation(summary = "Send a communication to the queue")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Communication sent to queue successfully"),
        @ApiResponse(responseCode = "404", description = "Communication not found", content = @Content)
    })
    @GetMapping("/sendToQueue/{id}")
    public void sendCommunication(
        @Parameter(description = "ID of the communication to be sent to the queue", required = true)
        @PathVariable Long id) throws CommunicationNotFoundException {
        log.info(">>> sendCommunication");
        service.sendCommunication(id);
    }
}
