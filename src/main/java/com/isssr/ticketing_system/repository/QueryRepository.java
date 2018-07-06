package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.auto_generated.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryRepository extends JpaRepository<Query, Long> {

    Page<Query> findAll(Pageable pageable);

    Page<Query> findAllByDeleted(boolean isDeleted, Pageable pageable);

    List<Query> findAllByActive(boolean isActive);
}
