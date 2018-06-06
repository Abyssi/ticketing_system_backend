package com.isssr.ticketing_system.controller.mailController;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Configuration
@Component
@Controller
public abstract class MailController implements IMailController {

    //IMAP server settings -----------

    @Value("${mail.receiver.protocol}")
    protected String protocol;

    @Value("${mail.receiver.host}")
    protected String receiverHost;

    @Value("${mail.receiver.port}")
    protected String port;

    @Value("${mail.receiver.attachmentDirectory}")
    protected String saveDirectory;

    //Sender setting ----------

    @Value("${mail.sender.host}")
    protected String senderHost;

    //E-mail credentials ---------

    @Value("${mail.username}")
    protected String userName;

    @Value("${mail.password}")
    protected String password;

    public abstract void sendMail(String address, String mailType);

    public abstract void receiveMail();

    public abstract boolean isServerRunning();
}
