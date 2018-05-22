package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.TicketRelation;
import com.isssr.ticketing_system.repository.TicketRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketRelationService {
    @Autowired
    private TicketRelationRepository ticketRelationRepository;

    @Transactional
    public TicketRelation save(TicketRelation ticketRelation) {
        return this.ticketRelationRepository.save(ticketRelation);
    }

    @Transactional
    public Optional<TicketRelation> findById(Long id) {
        return this.ticketRelationRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.ticketRelationRepository.existsById(id);
    }

    @Transactional
    public Iterable<TicketRelation> findAll() {
        return this.ticketRelationRepository.findAll();
    }

    @Transactional
    public Iterable<TicketRelation> findAllById(Iterable<Long> ids) {
        return this.ticketRelationRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.ticketRelationRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.ticketRelationRepository.existsById(id);
        if (exists) this.ticketRelationRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.ticketRelationRepository.deleteAll();
    }
}
