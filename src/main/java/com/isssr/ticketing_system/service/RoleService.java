package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.Role;
import com.isssr.ticketing_system.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public Role save(Role role) {
        if (role.getId() == null && this.roleRepository.existsByName(role.getName()))
            role.setId(this.findByName(role.getName()).get().getId());
        return this.roleRepository.save(role);
    }

    @Transactional
    public Optional<Role> findById(Long id) {
        return this.roleRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.roleRepository.existsById(id);
    }

    @Transactional
    public Iterable<Role> findAll() {
        return this.roleRepository.findAll();
    }

    @Transactional
    public Iterable<Role> findAllById(Iterable<Long> ids) {
        return this.roleRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.roleRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.roleRepository.existsById(id);
        if (exists) this.roleRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.roleRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    @Transactional
    public Optional<Role> findByName(String name) {
        return this.roleRepository.findByName(name);
    }
}
