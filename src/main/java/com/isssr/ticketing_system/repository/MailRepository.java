package com.isssr.ticketing_system.repository;

import com.isssr.ticketing_system.model.Mail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailRepository extends CrudRepository<Mail, Long> {
    Optional<Mail> findByType(String type);

    boolean existsByType(String type);
}
