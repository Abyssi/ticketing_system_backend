package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.TicketEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketEventRepository extends CrudRepository<TicketEvent, Long> {
}
