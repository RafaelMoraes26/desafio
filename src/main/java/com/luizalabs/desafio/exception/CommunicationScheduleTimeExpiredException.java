package com.luizalabs.desafio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommunicationScheduleTimeExpiredException extends RuntimeException {
    public CommunicationScheduleTimeExpiredException(String message) {
        super(message);
    }
}
