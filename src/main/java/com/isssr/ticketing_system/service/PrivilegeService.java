package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.Privilege;
import com.isssr.ticketing_system.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PrivilegeService {
    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Transactional
    public Privilege save(Privilege privilege) {
        if (privilege.getId() == null && this.privilegeRepository.existsByName(privilege.getName()))
            privilege.setId(this.findByName(privilege.getName()).get().getId());
        return this.privilegeRepository.save(privilege);
    }

    @Transactional
    public Optional<Privilege> findById(Long id) {
        return this.privilegeRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.privilegeRepository.existsById(id);
    }

    @Transactional
    public Iterable<Privilege> findAll() {
        return this.privilegeRepository.findAll();
    }

    @Transactional
    public Iterable<Privilege> findAllById(Iterable<Long> ids) {
        return this.privilegeRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.privilegeRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.privilegeRepository.existsById(id);
        if (exists) this.privilegeRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.privilegeRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.privilegeRepository.existsByName(name);
    }

    @Transactional
    public Optional<Privilege> findByName(String name) {
        return this.privilegeRepository.findByName(name);
    }
}
