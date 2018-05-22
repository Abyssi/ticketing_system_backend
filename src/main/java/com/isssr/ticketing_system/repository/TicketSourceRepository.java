package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.TicketSource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketSourceRepository extends CrudRepository<TicketSource, Long> {
    Optional<TicketSource> findByName(String name);

    boolean existsByName(String name);
}
