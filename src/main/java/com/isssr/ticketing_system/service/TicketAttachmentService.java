package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.TicketAttachment;
import com.isssr.ticketing_system.repository.TicketAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketAttachmentService {
    @Autowired
    private TicketAttachmentRepository ticketAttachmentRepository;

    @Transactional
    public TicketAttachment save(TicketAttachment ticketAttachment) {
        return this.ticketAttachmentRepository.save(ticketAttachment);
    }

    @Transactional
    public Optional<TicketAttachment> findById(Long id) {
        return this.ticketAttachmentRepository.findById(id);
    }

    @Transactional
    public boolean existsById(Long id) {
        return this.ticketAttachmentRepository.existsById(id);
    }

    @Transactional
    public Iterable<TicketAttachment> findAll() {
        return this.ticketAttachmentRepository.findAll();
    }

    @Transactional
    public Iterable<TicketAttachment> findAllById(Iterable<Long> ids) {
        return this.ticketAttachmentRepository.findAllById(ids);
    }

    @Transactional
    public long count() {
        return this.ticketAttachmentRepository.count();
    }

    @Transactional
    public boolean deleteById(Long id) {
        boolean exists = this.ticketAttachmentRepository.existsById(id);
        if (exists) this.ticketAttachmentRepository.deleteById(id);
        return exists;
    }

    @Transactional
    public void deleteAll() {
        this.ticketAttachmentRepository.deleteAll();
    }
}
