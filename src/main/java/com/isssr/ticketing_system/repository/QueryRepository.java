package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.auto_generated.temporary.DataBaseTimeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryRepository extends JpaRepository<DataBaseTimeQuery, Long> {

    Page<DataBaseTimeQuery> findAll(Pageable pageable);

    @Query("SELECT q FROM DataBaseTimeQuery q where q.deleted = false")
    Page<DataBaseTimeQuery> findAllNotDeleted(Pageable pageable);

    @Query("SELECT q FROM DataBaseTimeQuery q where q.deleted = true")
    Page<DataBaseTimeQuery> findAllDeleted(Pageable pageable);

    List<DataBaseTimeQuery> findAllByActiveTrue();

}
