package com.isssr.ticketing_system.controller.mailController;

public interface IMailController {

    //Specify receiver's address and the mail type
    public void sendMail(String address, String mailType);

    //Force imap server to scan INBOX folder for new email
    //Avoiding scheduler
    public void receiveMail();

    //Check if server is running
    public boolean isServerRunning();
}
