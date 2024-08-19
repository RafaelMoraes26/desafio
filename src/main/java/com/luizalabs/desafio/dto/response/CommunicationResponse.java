package com.luizalabs.desafio.dto.response;

import java.time.LocalDateTime;

import com.luizalabs.desafio.enums.CommunicationSendStatus;
import com.luizalabs.desafio.enums.CommunicationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunicationResponse {

    private Long id;
    private String message;
    private String receiver;
    private LocalDateTime scheduledTime;
    private CommunicationType type;
    private CommunicationSendStatus status;
}
