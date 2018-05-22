package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findByName(String name);

    boolean existsByName(String name);

    Page<Team> findAll(Pageable pageable);
}
