package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.logger.aspect.LogOperation;
import com.isssr.ticketing_system.model.SoftDelete.SoftDelete;
import com.isssr.ticketing_system.model.SoftDelete.SoftDeleteKind;
import com.isssr.ticketing_system.model.Team;
import com.isssr.ticketing_system.repository.TeamRepository;
import com.isssr.ticketing_system.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@SoftDelete(SoftDeleteKind.NOT_DELETED)
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PageableUtils pageableUtils;

    @Transactional
    @LogOperation(tag = "TEAM_CREATE", inputArgs = {"team"})
    public Team save(Team team) {
        return this.teamRepository.save(team);
    }

    @Transactional
    public Team updateById(@NotNull Long id, @NotNull Team team) throws EntityNotFoundException {
        if (!teamRepository.existsById(id))
            throw new EntityNotFoundException("Team to update not found in DB, maybe you have to create a new one");

        team.setId(id);
        return teamRepository.save(team);
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
        if (exists) {
            Team team = this.teamRepository.getOne(id);
            if (team.isDeleted()) {
                this.teamRepository.deleteById(id);
            } else {
                team.delete();
                this.teamRepository.save(team);
            }
        }
        return exists;
    }

    @Transactional
    public Team restoreById(Long id) throws EntityNotFoundException {

        if (this.teamRepository.existsById(id)) {

            Team team = this.teamRepository.getOne(id);

            team.restore();

            return this.teamRepository.save(team);

        } else {
            throw new EntityNotFoundException("Trying to restore Target not present in db");
        }

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
    @SoftDelete(SoftDeleteKind.ALL)
    public Page<Team> findAll(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Team> retrievedPage = this.teamRepository.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    @SoftDelete(SoftDeleteKind.NOT_DELETED)
    public Page<Team> findAllNotDeleted(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Team> retrievedPage = this.teamRepository.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    @SoftDelete(SoftDeleteKind.DELETED)
    public Page<Team> findAllDeleted(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Team> retrievedPage = this.teamRepository.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }
}
