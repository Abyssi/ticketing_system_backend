package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.logger.aspect.LogOperation;
import com.isssr.ticketing_system.model.User;
import com.isssr.ticketing_system.repository.UserRepository;
import com.isssr.ticketing_system.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PageableUtils pageableUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User create(User user) {
        return this.userRepository.save(user);
    }

    @Transactional
    @LogOperation(tag = "USER_CREATE", inputArgs = {"user"})
    public User save(User user) {
        if (user.getId() == null && this.userRepository.existsByEmail(user.getEmail()))
            user.setId(this.findByEmail(user.getEmail()).get().getId());

        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));

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
    @LogOperation(tag = "USER_UPDATE", inputArgs = {"user"})
    public User updateById(@NotNull Long id, @NotNull User user) throws EntityNotFoundException {
        if (!userRepository.existsById(id))
            throw new EntityNotFoundException("User to update not found in DB, maybe you have to create a new one");

        user.setId(id);

        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
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
    @LogOperation(tag = "USER_UPDATE", inputArgs = {"user"})
    public User updateByEmail(@NotNull String email, @NotNull User user) throws EntityNotFoundException {
        if (!userRepository.existsByEmail(email))
            throw new EntityNotFoundException("User to update not found in DB, maybe you have to create a new one");

        Optional<User> foundUser = userRepository.findByEmail(email);
        user.setId(foundUser.get().getId());

        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public Page<User> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    @Transactional
    public Page<User> findAll(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException, EntityNotFoundException {
        Page<User> retrievedPage = this.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
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

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
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


    public User updateUser(String term, String type, User user) throws UpdateException, EntityNotFoundException {
        switch (type) {
            case "id":
                return updateById(Long.parseLong(term), user);
            case "email":
                return updateByEmail(term, user);
        }
        throw new UpdateException("Bad term");
    }
}
