package com.isssr.ticketing_system.mail.mailHandler;

public interface IMailHandler {

    //Specify receiver's address and the mail type
    public void sendMail(String address, String mailType);

    //Make a custom e-mail with a certain text
    public void sendMail(String address, String mailType, String text);

    //make imap server to scan INBOX folder for new email
    public void receiveMail();

    //Check if server is running
    public boolean isServerRunning();
}
