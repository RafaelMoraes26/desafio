package com.luizalabs.desafio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommunicationNotFoundException extends RuntimeException {
    public CommunicationNotFoundException(String message) {
        super(message);
    }
}
