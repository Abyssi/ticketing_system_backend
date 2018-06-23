package com.isssr.ticketing_system.controller.mailController;

import com.isssr.ticketing_system.exception.FormatNotRespectedException;
import com.isssr.ticketing_system.exception.MailRejectedException;
import com.isssr.ticketing_system.model.*;
import com.isssr.ticketing_system.service.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.Properties;

@NoArgsConstructor
@Controller
@EnableScheduling
public class MailReceiverController extends MailController {

    private MimeBodyPart bodyPart;
    private String fileName;
    private boolean flag;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketSourceService ticketSourceService;

    @Autowired
    private TicketStatusService ticketStatusService;

    @Autowired
    private VisibilityService visibilityService;

    @Autowired
    private TicketPriorityService ticketPriorityService;

    @Autowired
    private TargetService productService;

    @Autowired
    private TicketCategoryService ticketCategoryService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private TicketAttachmentService ticketAttachmentService;

    @Autowired
    private MailSenderController mailSenderController;

    //Returns a Properties object which is configured for a IMAP server
    private Properties getServerProperties(String host, String port) {
        Properties properties = new Properties();

        // server setting
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", port);

        // SSL setting
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.socketFactory.fallback", "true");
        properties.setProperty("mail.imap.socketFactory.port", String.valueOf(port));
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.starttls.enable", "true");
        properties.put("mail.imap.ssl.trust", "*");

        return properties;
    }

    //Waiting for e-mails
    //@Scheduled(fixedDelay = 10000)
    public void receiveMail() {
        //System.out.println("Reading emails...");
        Properties properties = getServerProperties(receiverHost, port);
        Session session = Session.getDefaultInstance(properties);

        try {
            // connects to the message store
            Store store = session.getStore("imap");
            store.connect(receiverHost, userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_WRITE);

            // fetches new messages from server
            Message[] messages = folderInbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            TicketSource ticketSource = null;
            TicketStatus ticketStatus = null;
            Visibility visibility = null;
            User customer;
            if (messages.length != 0) {
                ticketSource = ticketSourceService.findByName("MAIL").get();
                ticketStatus = ticketStatusService.findByName("PENDING").get();
                visibility = visibilityService.findByName("PRIVATE").get();
            }

            //Read all messages in INBOX
            for (int i = 0; i < messages.length; i++) {

                Message msg = messages[i];
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                from = from.substring(from.indexOf("<") + 1, from.indexOf(">"));

                //Checking sender address, looking for match in db
                if (!checkAddress(from)) {
                    if (!checkDomain(from)) {
                        msg.setFlag(Flags.Flag.SEEN, true);
                        throw new MailRejectedException("***** E-mail rejected ******");
                    }
                    else customer = null;
                } else customer = userService.findByEmail(from.toLowerCase().trim()).get();


                /*
                if (!assignee.isPresent()){
                    msg.setFlag(Flags.Flag.SEEN, true);
                    throw new MailRejectedException("***** E-mail rejected ******");
                }*/

                String subject = msg.getSubject();
                String toList = parseAddresses(msg.getRecipients(RecipientType.TO));
                String ccList = parseAddresses(msg.getRecipients(RecipientType.CC));
                String sentDate = msg.getSentDate().toString();

                String messageContent = null;
                try {
                    messageContent = getTextFromMessage(msg);
                } catch (IOException e) {
                    msg.setFlag(Flags.Flag.SEEN, true);
                    e.printStackTrace();
                }

                // print out details of each message
                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("\t From: " + from);
                System.out.println("\t To: " + toList);
                System.out.println("\t CC: " + ccList);
                System.out.println("\t Subject: " + subject);
                System.out.println("\t Sent Date: " + sentDate);
                System.out.println("\t Message: " + messageContent);

                if (isFormatted(messageContent)) {

                    if (parseFormattedEmail(subject, messageContent, ticketSource, ticketStatus, visibility, customer) == null) {
                        this.mailSenderController.sendMail(from, "FORMAT");
                        throw new MailRejectedException("***** Sintax Error *****");
                    }
                    else this.mailSenderController.sendMail(from, "TICKET_OPENED");
                } else {
                    //Send email response
                    this.mailSenderController.sendMail(from, "FORMAT");
                }

                //Set email as read
                msg.setFlag(Flags.Flag.SEEN, true);
            }

            // disconnect
            folderInbox.close(false);
            store.close();

        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (MailRejectedException e) {
            System.out.println("Email rejected");
        }
    }

    private boolean checkDomain(String from) {
        return this.companyService.existsByDomain(from.substring(from.indexOf("@") + 1));
    }

    //Returns a list of addresses in String format separated by comma
    private String parseAddresses(Address[] address) {
        String listAddress = "";

        if (address != null) {
            for (Address addres : address) {
                listAddress += addres.toString() + ", ";
            }
        }
        if (listAddress.length() > 1) {
            listAddress = listAddress.substring(0, listAddress.length() - 2);
        }

        return listAddress;
    }

    //Get text from message, plain
    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            this.flag = false;
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    //get text from message, multipart
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            this.bodyPart = (MimeBodyPart) mimeMultipart.getBodyPart(i);
            if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                //save attachment file name and his bodypart
                this.flag = true;
                this.fileName = bodyPart.getFileName();
                bodyPart.saveFile(saveDirectory + File.separator + fileName);
            } else if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    //Switch between formatted and unformatted email
    private boolean isFormatted(String content) {
        String[] ticketAttribute = new String[]{"descrizione", "categoria", "target", "priorita"};

        //Avoid case sensitive match error
        content = content.toLowerCase();

        for (String aTicketAttribute : ticketAttribute) {
            if (!content.contains(aTicketAttribute + ":"))
                if (!content.contains(aTicketAttribute + " :"))
                    return false;
        }
        return true;
    }

    //Check email sender
    private boolean checkAddress(String sender) {
        return userService.existsByEmail(sender);
    }

    //Parser for a formatted e-mail
    //returns a new ticket to insert in the db
    private Ticket parseFormattedEmail(String subject, String content, TicketSource ticketSource, TicketStatus ticketStatus, Visibility visibility, User assignee) {
        Ticket ticket = new Ticket();
        try {
            //Get message's lines split
            String[] lines = content.split("\n");

            //Get right formatted text
            String productID = lines[1].substring(lines[1].indexOf(": ") + 1).toLowerCase();
            String category0 = lines[2].substring(lines[2].indexOf(": ") + 1).toLowerCase();
            String priority = lines[3].substring(lines[3].indexOf(": ") + 1).toLowerCase();
            String description = lines[4].substring(lines[4].indexOf(": ") + 1).toLowerCase();
            for (int i = 5; i < lines.length; i++) {
                description += lines[i];
            }

            Optional<TicketCategory> category = ticketCategoryService.findByName(category0.toUpperCase().trim());
            //Check existing category, throw exception otherwise
            if (!category.isPresent()) throw new FormatNotRespectedException("Format not respected");

            Optional<TicketPriority> ticketPriority = ticketPriorityService.findByName(priority.toUpperCase().trim());
            if (!ticketPriority.isPresent()) throw new FormatNotRespectedException("Format not respected");

            Optional<Target> product = productService.findByName(productID.toLowerCase().trim());
            if (!product.isPresent()) throw new FormatNotRespectedException("Format not respected");

            //Setting ticket's default values and retrieved ones
            ticket.setTitle(subject);
            ticket.setDescription(description);
            ticket.setCustomerPriority(ticketPriority.get());
            ticket.setTarget(product.get());
            ticket.setCategory(category.get());
            ticket.setStatus(ticketStatus);
            ticket.setSource(ticketSource);
            ticket.setVisibility(visibility);
            ticket.setCreationTimestamp(Instant.now());
            if (assignee != null) ticket.setCustomer(assignee);
            ticket.setAssignee(assignee);

            this.ticketService.save(ticket);

            TicketAttachment ticketAttachment = new TicketAttachment();
            //Check if exists attachment and save it
            if (this.flag) {
                bodyPart.saveFile(System.getProperty("user.dir") + saveDirectory + File.separator + fileName);
                ticketAttachment.setFileName(fileName);
                ticketAttachment.setTimestamp(Instant.now());
                ticketAttachment.setTicket(ticket);
                this.ticketAttachmentService.save(ticketAttachment);
                this.flag = false;
            }

        } catch (Exception e) {
            System.out.println("Email rejected, format not respected");
            return null;
        }
        return ticket;
    }

    @Override
    public void sendMail(String address, String mailType) {
    }

    @Override
    public boolean isServerRunning() {
        return true;
    }
}
