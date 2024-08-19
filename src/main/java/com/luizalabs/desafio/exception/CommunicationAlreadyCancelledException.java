package com.luizalabs.desafio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommunicationAlreadyCancelledException extends RuntimeException {
    public CommunicationAlreadyCancelledException(String message) {
        super(message);
    }
}
