package com.isssr.ticketing_system.mail;

import com.isssr.ticketing_system.mail.model.Mail;
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
