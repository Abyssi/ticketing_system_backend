package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.Target;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {
    Optional<Target> findByName(String name);

    boolean existsByName(String name);

    Page<Target> findAll(Pageable pageable);
}
