package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findAll(Pageable pageable);

    @Query("SELECT t FROM Ticket t where t.deleted = false")
    Page<Ticket> findAllNotDeleted(Pageable pageable);

    @Query("SELECT t FROM Ticket t where t.deleted = true")
    Page<Ticket> findAllDeleted(Pageable pageable);

    Page<Ticket> findByTitleContaining(String title, Pageable pageable);


}
