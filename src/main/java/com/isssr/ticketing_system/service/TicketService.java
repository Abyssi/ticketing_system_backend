package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.model.Ticket;
import com.isssr.ticketing_system.repository.TicketRepository;
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
        if (exists) this.ticketRepository.deleteById(id);
        return exists;
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
    public Page<Ticket> findAll(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException, EntityNotFoundException {
        Page<Ticket> retrievedPage = this.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }
}
