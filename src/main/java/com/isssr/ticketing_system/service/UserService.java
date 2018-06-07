package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.User;
import com.isssr.ticketing_system.repository.UserRepository;
import com.isssr.ticketing_system.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PageableUtils pageableUtils;

    @Transactional
    public User create(User user) {
        return this.userRepository.save(user);
    }

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
    public User updateUser(@NotNull Long id, @NotNull User user) throws EntityNotFoundException, UpdateException {

        Optional<User> updatingUser = findById(id);

        if (!updatingUser.isPresent())
            throw new EntityNotFoundException("User to update not found in DB, maybe you have to create a new one");

        User toUpdate = updatingUser.get();

        toUpdate.updateMe(user);

        return this.userRepository.save(toUpdate);

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

    @Transactional
    public Page<User> findAll(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException, EntityNotFoundException {
        Page<User> retrievedPage = this.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    public Page<User> findByEmailContaining(@NotNull String email, Pageable pageable) {
        return this.userRepository.findByEmailContaining(email, pageable);
    }

    @Transactional
    public Page<User> findByEmailContaining(@NotNull String email, @NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException, EntityNotFoundException {
        Page<User> retrievedPage = this.findByEmailContaining(email, pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    public Optional<User> findUser(String term, String type) {
        switch (type) {
            case "id":
                return findById(Long.parseLong(term));
            case "email":
                return findByEmail(term);
        }
        return Optional.empty();
    }
}
