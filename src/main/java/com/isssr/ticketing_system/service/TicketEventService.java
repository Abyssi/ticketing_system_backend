package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.TicketEvent;
import com.isssr.ticketing_system.repository.TicketEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketEventService {
    @Autowired
    private TicketEventRepository ticketEventRepository;

    @Transactional
    public TicketEvent save(TicketEvent ticketEvent) {
        return this.ticketEventRepository.save(ticketEvent);
    }

    @Transactional
    public Optional<TicketEvent> findById(Long id) {
        return this.ticketEventRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.ticketEventRepository.existsById(id);
    }

    @Transactional
    public Iterable<TicketEvent> findAll() {
        return this.ticketEventRepository.findAll();
    }

    @Transactional
    public Iterable<TicketEvent> findAllById(Iterable<Long> ids) {
        return this.ticketEventRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.ticketEventRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.ticketEventRepository.existsById(id);
        if (exists) this.ticketEventRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.ticketEventRepository.deleteAll();
    }
}
