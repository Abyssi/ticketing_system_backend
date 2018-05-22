package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.TicketDifficulty;
import com.isssr.ticketing_system.repository.TicketDifficultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketDifficultyService {
    @Autowired
    private TicketDifficultyRepository ticketDifficultyRepository;

    @Transactional
    public TicketDifficulty save(TicketDifficulty ticketDifficulty) {
        if (ticketDifficulty.getId() == null && this.ticketDifficultyRepository.existsByName(ticketDifficulty.getName()))
            ticketDifficulty.setId(this.findByName(ticketDifficulty.getName()).get().getId());
        return this.ticketDifficultyRepository.save(ticketDifficulty);
    }

    @Transactional
    public Optional<TicketDifficulty> findById(Long id) {
        return this.ticketDifficultyRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.ticketDifficultyRepository.existsById(id);
    }

    @Transactional
    public Iterable<TicketDifficulty> findAll() {
        return this.ticketDifficultyRepository.findAll();
    }

    @Transactional
    public Iterable<TicketDifficulty> findAllById(Iterable<Long> ids) {
        return this.ticketDifficultyRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.ticketDifficultyRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.ticketDifficultyRepository.existsById(id);
        if (exists) this.ticketDifficultyRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.ticketDifficultyRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.ticketDifficultyRepository.existsByName(name);
    }

    @Transactional
    public Optional<TicketDifficulty> findByName(String name) {
        return this.ticketDifficultyRepository.findByName(name);
    }
}
