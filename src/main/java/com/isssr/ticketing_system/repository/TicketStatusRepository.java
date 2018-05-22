package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.TicketStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketStatusRepository extends CrudRepository<TicketStatus, Long> {
    Optional<TicketStatus> findByName(String name);

    boolean existsByName(String name);
}
