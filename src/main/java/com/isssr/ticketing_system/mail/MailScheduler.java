package com.isssr.ticketing_system.mail;

import com.isssr.ticketing_system.mail.mailHandler.MailReceiverHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class MailScheduler {

    @Autowired
    private MailReceiverHandler mailReceiverHandler;

    @Scheduled(fixedDelay = 10000)
    public void startMailScheduling() {
        mailReceiverHandler.receiveMail();
    }
}
