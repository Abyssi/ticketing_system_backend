package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.TicketComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketCommentRepository extends CrudRepository<TicketComment, Long> {
}
