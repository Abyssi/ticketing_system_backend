package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.TicketCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketCategoryRepository extends CrudRepository<TicketCategory, Long> {
    Optional<TicketCategory> findByName(String name);

    boolean existsByName(String name);
}
