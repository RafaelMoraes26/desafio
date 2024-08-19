package com.luizalabs.desafio.dto.request;

import java.time.LocalDateTime;

import com.luizalabs.desafio.enums.CommunicationType;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CommunicationRequest {

    @NotBlank
    private String message;
    @NotBlank
    private String receiver;
    @NotNull
    private LocalDateTime scheduledTime;
    @NotNull
    private CommunicationType type;
}
