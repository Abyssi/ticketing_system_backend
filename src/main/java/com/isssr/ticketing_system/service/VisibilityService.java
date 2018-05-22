package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.Visibility;
import com.isssr.ticketing_system.repository.VisibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VisibilityService {
    @Autowired
    private VisibilityRepository visibilityRepository;

    @Transactional
    public Visibility save(Visibility ticketVisibility) {
        if (ticketVisibility.getId() == null && this.visibilityRepository.existsByName(ticketVisibility.getName()))
            ticketVisibility.setId(this.findByName(ticketVisibility.getName()).get().getId());
        return this.visibilityRepository.save(ticketVisibility);
    }

    @Transactional
    public Optional<Visibility> findById(Long id) {
        return this.visibilityRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.visibilityRepository.existsById(id);
    }

    @Transactional
    public Iterable<Visibility> findAll() {
        return this.visibilityRepository.findAll();
    }

    @Transactional
    public Iterable<Visibility> findAllById(Iterable<Long> ids) {
        return this.visibilityRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.visibilityRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.visibilityRepository.existsById(id);
        if (exists) this.visibilityRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.visibilityRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.visibilityRepository.existsByName(name);
    }

    @Transactional
    public Optional<Visibility> findByName(String name) {
        return this.visibilityRepository.findByName(name);
    }
}
