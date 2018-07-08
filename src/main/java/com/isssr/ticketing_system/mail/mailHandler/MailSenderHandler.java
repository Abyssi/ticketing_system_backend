package com.isssr.ticketing_system.mail.mailHandler;

import com.isssr.ticketing_system.logger.aspect.LogOperation;
import com.isssr.ticketing_system.mail.model.Mail;
import com.isssr.ticketing_system.mail.MailService;
import org.apache.commons.mail.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MailSenderHandler extends MailHandler {

    //Thread Attribute
    private String address;
    private String mailType;
    private String mailText;

    @Autowired
    private MailService mailService;

    @LogOperation(tag = "MAIL_SEND", inputArgs = "mailType")
    public void sendMail(String address, String mailType){
        //init attribute
        this.address = address;
        this.mailType = mailType;
        this.mailText = null;

        //Start thread
        (new Thread(this)).start();
    }

    public void run() {
        try {
            //Query to db for retrieve subject and content email, by type
            Mail mail = this.mailService.findByType(mailType).get();

            //Build email
            MultiPartEmail email = new MultiPartEmail();
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator(userName, password));
            email.setHostName("smtp.gmail.com");
            email.setFrom(userName);
            email.setSubject(mail.getSubject());
            if (mailText == null) email.setMsg(mail.getDescription());
            else email.setMsg(mail.getDescription() + "\n\n" + mailText);
            email.addTo(address);
            email.setTLS(true);

            //Check email type for adding attach
            if (mailType.equals("FORMAT")){
                // Create the attachment
                email.attach(new File(System.getProperty("user.dir") + saveDirectory + File.separator + attach));
            }

            //Send e-mail
            email.send();
            System.out.println("Response e-mail sent!");
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
        }
    }

    @LogOperation(tag = "MAIL_SEND", inputArgs = "mailType")
    public void sendMail(String address, String mailType, String mailText) {
        //init attribute
        this.address = address;
        this.mailType = mailType;
        this.mailText = mailText;

        //Start thread
        (new Thread(this)).start();
    }

    @Override
    public void receiveMail() {
    }

    @Override
    public boolean isServerRunning() {
        return false;
    }
}


