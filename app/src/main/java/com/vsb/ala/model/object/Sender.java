package com.vsb.ala.model.object;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Sender {
    public String sEmail = "ala.invoice.noreply@gmail.com";
    public String sPassword = "nyqccdebmmylzxod";
    public String stringHost = "smtp.gmail.com";

    MimeMessage mimeMessage;
    Properties properties;
    Multipart multipart = new MimeMultipart();
    MimeBodyPart body = new MimeBodyPart();

    public void setProperties(){
        properties = System.getProperties();
        properties.put("mail.smtp.host", stringHost);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth", "true");

    }

    public void setSession(){
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sEmail,sPassword);
            }
        });

        mimeMessage = new MimeMessage(session);
    }

    public void setRecieverEmail(String rEmail){
        try {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(rEmail));
        }catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void setSubjectEmail(String subject){
        try {
            mimeMessage.setSubject(subject);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void setBodyEmail(String body){
        try {
            this.body.setText(body);
            multipart.addBodyPart(this.body);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void addAttachFile(File file){
        MimeBodyPart attach = new MimeBodyPart();
        try {
            attach.attachFile(file);
            multipart.addBodyPart(attach);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public void sendEmail(){
        try {
            mimeMessage.setContent(multipart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Transport.send(mimeMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

}
