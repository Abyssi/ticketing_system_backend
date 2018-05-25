package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.model.Team;
import com.isssr.ticketing_system.repository.TeamRepository;
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
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PageableUtils pageableUtils;

    @Transactional
    public Team save(Team team) {
        if (team.getId() == null && this.teamRepository.existsByName(team.getName()))
            team.setId(this.findByName(team.getName()).get().getId());
        return this.teamRepository.save(team);
    }

    @Transactional
    public Optional<Team> findById(Long id) {
        return this.teamRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.teamRepository.existsById(id);
    }

    @Transactional
    public Iterable<Team> findAll() {
        return this.teamRepository.findAll();
    }

    @Transactional
    public Iterable<Team> findAllById(Iterable<Long> ids) {
        return this.teamRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.teamRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.teamRepository.existsById(id);
        if (exists) this.teamRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.teamRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.teamRepository.existsByName(name);
    }

    @Transactional
    public Optional<Team> findByName(String name) {
        return this.teamRepository.findByName(name);
    }

    @Transactional
    public Page<Team> findAll(Pageable pageable) {
        return this.teamRepository.findAll(pageable);
    }

    @Transactional
    public Page<Team> findAll(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException, EntityNotFoundException {
        Page<Team> retrievedPage = this.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }
}
