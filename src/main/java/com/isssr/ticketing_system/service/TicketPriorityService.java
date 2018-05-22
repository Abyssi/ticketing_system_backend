package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.repository.TicketPriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketPriorityService {
    @Autowired
    private TicketPriorityRepository ticketPriorityRepository;

    @Transactional
    public TicketPriority save(TicketPriority ticketPriority) {
        if (ticketPriority.getId() == null && this.ticketPriorityRepository.existsByName(ticketPriority.getName()))
            ticketPriority.setId(this.findByName(ticketPriority.getName()).get().getId());
        return this.ticketPriorityRepository.save(ticketPriority);
    }

    @Transactional
    public Optional<TicketPriority> findById(Long id) {
        return this.ticketPriorityRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.ticketPriorityRepository.existsById(id);
    }

    @Transactional
    public Iterable<TicketPriority> findAll() {
        return this.ticketPriorityRepository.findAll();
    }

    @Transactional
    public Iterable<TicketPriority> findAllById(Iterable<Long> ids) {
        return this.ticketPriorityRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.ticketPriorityRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.ticketPriorityRepository.existsById(id);
        if (exists) this.ticketPriorityRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.ticketPriorityRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.ticketPriorityRepository.existsByName(name);
    }

    @Transactional
    public Optional<TicketPriority> findByName(String name) {
        return this.ticketPriorityRepository.findByName(name);
    }
}
