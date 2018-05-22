package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.TicketDifficulty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketDifficultyRepository extends CrudRepository<TicketDifficulty, Long> {
    Optional<TicketDifficulty> findByName(String name);

    boolean existsByName(String name);
}
