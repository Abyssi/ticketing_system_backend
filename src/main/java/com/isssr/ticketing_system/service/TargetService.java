package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.SoftDelete.SoftDelete;
import com.isssr.ticketing_system.model.SoftDelete.SoftDeleteKind;
import com.isssr.ticketing_system.model.Target;
import com.isssr.ticketing_system.repository.TargetRepository;
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
public class TargetService {
    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private PageableUtils pageableUtils;

    @Transactional
    public Target save(Target target) {
        if (target.getId() == null && this.targetRepository.existsByName(target.getName()))
            target.setId(this.findByName(target.getName()).get().getId());
        return this.targetRepository.save(target);
    }

    @Transactional
    public @NotNull Target updateOne(@NotNull Long id, @NotNull Target updatedData) throws UpdateException, EntityNotFoundException {
        Target updatingTarget = targetRepository.getOne(id);

        if (updatingTarget == null)
            throw new EntityNotFoundException("Target to update not found in DB, maybe you have to create a new one");

        updatingTarget.updateMe(updatedData);

        return targetRepository.save(updatingTarget);
    }

    @Transactional
    public Optional<Target> findById(Long id) {
        return this.targetRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.targetRepository.existsById(id);
    }

    @Transactional
    public Iterable<Target> findAll() {
        return this.targetRepository.findAll();
    }

    @Transactional
    public Iterable<Target> findAllById(Iterable<Long> ids) {
        return this.targetRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.targetRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.targetRepository.existsById(id);

        if (exists) {

            Target target = this.targetRepository.getOne(id);

            if (target.isDeleted()) {

                this.targetRepository.deleteById(id);

            } else {

                target.delete();

                this.targetRepository.save(target);
            }
        }
        return exists;
    }

    @Transactional
    public Target restoreById(Long id) throws EntityNotFoundException {

        if (this.targetRepository.existsById(id)) {

            Target target = this.targetRepository.getOne(id);

            target.restore();

            return this.targetRepository.save(target);

        } else {
            throw new EntityNotFoundException("Trying to restore Target not present in db");
        }

    }

    @Transactional
    public void deleteAll() {
        this.targetRepository.deleteAll();
    }

    @Transactional
    public boolean existsByName(String name) {
        return this.targetRepository.existsByName(name);
    }

    @Transactional
    public Optional<Target> findByName(String name) {
        return this.targetRepository.findByName(name);
    }

    @Transactional
    @SoftDelete(SoftDeleteKind.ALL)
    public Page<Target> findAll(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Target> retrievedPage = this.targetRepository.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    @SoftDelete(SoftDeleteKind.NOT_DELETED)
    public Page<Target> findAllNotDeleted(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Target> retrievedPage = this.targetRepository.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    @SoftDelete(SoftDeleteKind.DELETED)
    public Page<Target> findAllDeleted(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Target> retrievedPage = this.targetRepository.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }
}
