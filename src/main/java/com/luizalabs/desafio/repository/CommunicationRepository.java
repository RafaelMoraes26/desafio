package com.luizalabs.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luizalabs.desafio.repository.entity.Communication;

public interface CommunicationRepository extends JpaRepository<Communication, Long> {
}
