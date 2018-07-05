package com.isssr.ticketing_system.controller.mailController;

import com.isssr.ticketing_system.logger.aspect.LogOperation;
import com.isssr.ticketing_system.model.Mail;
import com.isssr.ticketing_system.service.MailService;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MailSenderController extends MailController {

    @Autowired
    private MailService mailService;

    @Override
    @LogOperation(tag = "MAIL_SEND")
    public void sendMail(String address, String mailType) {
        try {
            //Query to db for retrieve subject and content email, by type
            Mail mail = this.mailService.findByType(mailType).get();

            //Build email
            Email email = new SimpleEmail();
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator(userName, password));
            email.setHostName("smtp.gmail.com");
            email.setFrom(userName);
            email.setSubject(mail.getSubject());
            email.setMsg(mail.getDescription());
            email.addTo(address);
            email.setTLS(true);
            email.send();
            System.out.println("Response e-mail sent!");
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
        }
    }


    @LogOperation(tag = "MAIL_SEND")
    public void sendMail(String address, String mailType, String mailText) {

        try {
            //Query to db for retrieve subject and content email, by type
            Mail mail = this.mailService.findByType(mailType).get();

            //Build email
            Email email = new SimpleEmail();
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator(userName, password));
            email.setHostName("smtp.gmail.com");
            email.setFrom(userName);
            email.setSubject(mail.getSubject());
            email.setMsg(mail.getDescription() + "\n\n" + mailText);
            email.addTo(address);
            email.setTLS(true);
            email.send();
            System.out.println("Response e-mail sent!");
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
        }
    }

    @Override
    public void receiveMail() {
    }

    @Override
    public boolean isServerRunning() {
        return false;
    }
}


