package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.User;
import com.isssr.ticketing_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User save(User user) {
        if (user.getId() == null && this.userRepository.existsByEmail(user.getEmail()))
            user.setId(this.findByEmail(user.getEmail()).get().getId());
        return this.userRepository.save(user);
    }

    @Transactional
    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.userRepository.existsById(id);
    }

    @Transactional
    public Iterable<User> findAll() {
        return this.userRepository.findAll();
    }

    @Transactional
    public Iterable<User> findAllById(Iterable<Long> ids) {
        return this.userRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.userRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.userRepository.existsById(id);
        if (exists) this.userRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.userRepository.deleteAll();
    }

    @Transactional
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Transactional
    public Page<User> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }
}
