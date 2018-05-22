package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.TicketRelation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRelationRepository extends CrudRepository<TicketRelation, Long> {
}
