package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.TicketCategory;
import com.isssr.ticketing_system.repository.TicketCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketCategoryService {
    @Autowired
    private TicketCategoryRepository ticketCategoryRepository;

    @Transactional
    public TicketCategory save(TicketCategory ticketCategory) {
        if (ticketCategory.getId() == null && this.ticketCategoryRepository.existsByName(ticketCategory.getName()))
            ticketCategory.setId(this.findByName(ticketCategory.getName()).get().getId());
        return this.ticketCategoryRepository.save(ticketCategory);
    }

    @Transactional
    public Optional<TicketCategory> findById(Long id) {
        return this.ticketCategoryRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.ticketCategoryRepository.existsById(id);
    }

    @Transactional
    public Iterable<TicketCategory> findAll() {
        return this.ticketCategoryRepository.findAll();
    }

    @Transactional
    public Iterable<TicketCategory> findAllById(Iterable<Long> ids) {
        return this.ticketCategoryRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.ticketCategoryRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.ticketCategoryRepository.existsById(id);
        if (exists) this.ticketCategoryRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.ticketCategoryRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.ticketCategoryRepository.existsByName(name);
    }

    @Transactional
    public Optional<TicketCategory> findByName(String name) {
        return this.ticketCategoryRepository.findByName(name);
    }
}
