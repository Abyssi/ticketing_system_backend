package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);

    boolean existsByName(String name);

    Page<Team> findAll(Pageable pageable);

    @Query("SELECT t FROM Team t where t.deleted = false")
    Page<Team> findAllNotDeleted(Pageable pageable);

    @Query("SELECT t FROM Team t where t.deleted = true")
    Page<Team> findAllDeleted(Pageable pageable);
}
