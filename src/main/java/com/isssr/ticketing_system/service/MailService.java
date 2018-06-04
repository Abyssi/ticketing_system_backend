package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.model.*;
import com.isssr.ticketing_system.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MailService {

    @Autowired
    private MailRepository mailRepository;

    @Transactional
    public Mail save(Mail mail) {
        return this.mailRepository.save(mail);
    }

    @Transactional
    public Optional<Mail> findByType(String type) {
        return this.mailRepository.findByType(type);
    }

    @Transactional
    public boolean existsByType(String type) {
        return this.mailRepository.existsByType(type);
    }
}
