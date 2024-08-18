package com.luizalabs.desafio.repository.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Communication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String receiver;
    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;
}
