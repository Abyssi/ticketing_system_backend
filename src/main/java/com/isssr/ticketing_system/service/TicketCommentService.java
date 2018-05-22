package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.TicketComment;
import com.isssr.ticketing_system.repository.TicketCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketCommentService {
    @Autowired
    private TicketCommentRepository ticketCommentRepository;

    @Transactional
    public TicketComment save(TicketComment ticketComment) {
        return this.ticketCommentRepository.save(ticketComment);
    }

    @Transactional
    public Optional<TicketComment> findById(Long id) {
        return this.ticketCommentRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.ticketCommentRepository.existsById(id);
    }

    @Transactional
    public Iterable<TicketComment> findAll() {
        return this.ticketCommentRepository.findAll();
    }

    @Transactional
    public Iterable<TicketComment> findAllById(Iterable<Long> ids) {
        return this.ticketCommentRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.ticketCommentRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.ticketCommentRepository.existsById(id);
        if (exists) this.ticketCommentRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.ticketCommentRepository.deleteAll();
    }
}
