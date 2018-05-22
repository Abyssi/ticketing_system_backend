package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.TicketStatus;
import com.isssr.ticketing_system.repository.TicketStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketStatusService {
    @Autowired
    private TicketStatusRepository ticketStatusRepository;

    @Transactional
    public TicketStatus save(TicketStatus ticketStatus) {
        if (ticketStatus.getId() == null && this.ticketStatusRepository.existsByName(ticketStatus.getName()))
            ticketStatus.setId(this.findByName(ticketStatus.getName()).get().getId());
        return this.ticketStatusRepository.save(ticketStatus);
    }

    @Transactional
    public Optional<TicketStatus> findById(Long id) {
        return this.ticketStatusRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.ticketStatusRepository.existsById(id);
    }

    @Transactional
    public Iterable<TicketStatus> findAll() {
        return this.ticketStatusRepository.findAll();
    }

    @Transactional
    public Iterable<TicketStatus> findAllById(Iterable<Long> ids) {
        return this.ticketStatusRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.ticketStatusRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.ticketStatusRepository.existsById(id);
        if (exists) this.ticketStatusRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.ticketStatusRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.ticketStatusRepository.existsByName(name);
    }

    @Transactional
    public Optional<TicketStatus> findByName(String name) {
        return this.ticketStatusRepository.findByName(name);
    }
}
