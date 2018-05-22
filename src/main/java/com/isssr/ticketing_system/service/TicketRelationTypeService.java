package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.TicketRelationType;
import com.isssr.ticketing_system.repository.TicketRelationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketRelationTypeService {
    @Autowired
    private TicketRelationTypeRepository ticketRelationTypeRepository;

    @Transactional
    public TicketRelationType save(TicketRelationType ticketRelationType) {
        if (ticketRelationType.getId() == null && this.ticketRelationTypeRepository.existsByName(ticketRelationType.getName()))
            ticketRelationType.setId(this.findByName(ticketRelationType.getName()).get().getId());
        return this.ticketRelationTypeRepository.save(ticketRelationType);
    }

    @Transactional
    public Optional<TicketRelationType> findById(Long id) {
        return this.ticketRelationTypeRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.ticketRelationTypeRepository.existsById(id);
    }

    @Transactional
    public Iterable<TicketRelationType> findAll() {
        return this.ticketRelationTypeRepository.findAll();
    }

    @Transactional
    public Iterable<TicketRelationType> findAllById(Iterable<Long> ids) {
        return this.ticketRelationTypeRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.ticketRelationTypeRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.ticketRelationTypeRepository.existsById(id);
        if (exists) this.ticketRelationTypeRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.ticketRelationTypeRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.ticketRelationTypeRepository.existsByName(name);
    }

    @Transactional
    public Optional<TicketRelationType> findByName(String name) {
        return this.ticketRelationTypeRepository.findByName(name);
    }
}
