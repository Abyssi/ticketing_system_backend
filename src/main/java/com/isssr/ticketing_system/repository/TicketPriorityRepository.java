package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.TicketPriority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketPriorityRepository extends CrudRepository<TicketPriority, Long> {
    Optional<TicketPriority> findByName(String name);

    boolean existsByName(String name);
}
