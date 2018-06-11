package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.model.Ticket;
import com.isssr.ticketing_system.repository.TicketRepository;
import com.isssr.ticketing_system.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PageableUtils pageableUtils;

    @Transactional
    public Ticket save(Ticket ticket) {
        return this.ticketRepository.save(ticket);
    }

    @Transactional
    public @NotNull Ticket updateOne(@NotNull Long id, @NotNull Ticket updatedData) throws UpdateException, EntityNotFoundException {
        Ticket updatingTicket = ticketRepository.getOne(id);

        if (updatingTicket == null)
            throw new EntityNotFoundException("Ticket to update not found in DB, maybe you have to create a new one");

        updatingTicket.updateMe(updatedData);

        return ticketRepository.save(updatingTicket);
    }

    @Transactional
    public Optional<Ticket> findById(Long id) {
        return this.ticketRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.ticketRepository.existsById(id);
    }

    @Transactional
    public Iterable<Ticket> findAll() {
        return this.ticketRepository.findAll();
    }

    @Transactional
    public Iterable<Ticket> findAllById(Iterable<Long> ids) {
        return this.ticketRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.ticketRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.ticketRepository.existsById(id);

        if (exists) {

            Ticket ticket = this.ticketRepository.getOne(id);

            if (ticket.isDeleted()) {

                this.ticketRepository.deleteById(id);

            } else {

                ticket.markMeAsDeleted();

                this.ticketRepository.save(ticket);
            }
        }
        return exists;
    }

    @Transactional
    public Ticket restoreById(Long id) throws EntityNotFoundException {

        if (this.ticketRepository.existsById(id)) {

            Ticket ticket = this.ticketRepository.getOne(id);

            ticket.restoreMe();

            return this.ticketRepository.save(ticket);

        } else {
            throw new EntityNotFoundException("Trying to restore Target not present in db");
        }

    }

    @Transactional
    public void deleteAll() {
        this.ticketRepository.deleteAll();
    }

    @Transactional
    public Page<Ticket> findAll(Pageable pageable) {
        return this.ticketRepository.findAll(pageable);
    }

    @Transactional
    public Page<Ticket> findAll(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Ticket> retrievedPage = this.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    public Page<Ticket> findAllNotDeleted(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Ticket> retrievedPage = this.findAllNotDeleted(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    public Page<Ticket> findAllNotDeleted(PageRequest pageRequest) {
        return this.ticketRepository.findAllNotDeleted(pageRequest);
    }

    @Transactional
    public Page<Ticket> findAllDeleted(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Ticket> retrievedPage = this.findAllDeleted(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    public Page<Ticket> findAllDeleted(PageRequest pageRequest) {
        return this.ticketRepository.findAllDeleted(pageRequest);
    }

    @Transactional
    public Page<Ticket> findByTitleContaining(@NotNull String title, Pageable pageable) {
        return this.ticketRepository.findByTitleContainingAndDeleted(title, false, pageable);
    }

    @Transactional
    public Page<Ticket> findByTitleContaining(@NotNull String title, @NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException, javax.persistence.EntityNotFoundException {
        Page<Ticket> retrievedPage = this.findByTitleContaining(title, pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }
}
