package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.TicketAttachment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketAttachmentRepository extends CrudRepository<TicketAttachment, Long> {
}
