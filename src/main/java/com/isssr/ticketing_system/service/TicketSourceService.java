package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.TicketSource;
import com.isssr.ticketing_system.repository.TicketSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketSourceService {
    @Autowired
    private TicketSourceRepository ticketSourceRepository;

    @Transactional
    public TicketSource save(TicketSource ticketSource) {
        if (ticketSource.getId() == null && this.ticketSourceRepository.existsByName(ticketSource.getName()))
            ticketSource.setId(this.findByName(ticketSource.getName()).get().getId());
        return this.ticketSourceRepository.save(ticketSource);
    }

    @Transactional
    public Optional<TicketSource> findById(Long id) {
        return this.ticketSourceRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.ticketSourceRepository.existsById(id);
    }

    @Transactional
    public Iterable<TicketSource> findAll() {
        return this.ticketSourceRepository.findAll();
    }

    @Transactional
    public Iterable<TicketSource> findAllById(Iterable<Long> ids) {
        return this.ticketSourceRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.ticketSourceRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.ticketSourceRepository.existsById(id);
        if (exists) this.ticketSourceRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.ticketSourceRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.ticketSourceRepository.existsByName(name);
    }

    @Transactional
    public Optional<TicketSource> findByName(String name) {
        return this.ticketSourceRepository.findByName(name);
    }
}
