package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.TicketRelationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRelationTypeRepository extends CrudRepository<TicketRelationType, Long> {
    Optional<TicketRelationType> findByName(String name);

    boolean existsByName(String name);
}
