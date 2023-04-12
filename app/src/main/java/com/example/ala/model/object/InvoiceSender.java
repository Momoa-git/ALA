 package com.example.ala.model.object;

import android.content.Context;
import android.widget.Toast;

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

public class InvoiceSender {

    Invoice invoice;
    Context context;

    public InvoiceSender(Invoice invoice, Context context){
       this.invoice = invoice;
       this.context = context;
    }


    public void sendInvoiceToEmail(File file)
    {

        String sEmail = "ala.invoice.noreply@gmail.com";
        String sPassword = "nyqccdebmmylzxod";
        String rEmail = invoice.order.getCustomer_email();

        String stringHost = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", stringHost);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        //  properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.auth", "true");




        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sEmail,sPassword);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(rEmail));
            mimeMessage.setSubject("Informace o vyřízení objednávky č. " + invoice.order.getOrder_number());

            Multipart multipart = new MimeMultipart();
            MimeBodyPart body = new MimeBodyPart();
            body.setText("Dobrý den, \n" +
                    "děkujeme, že jste využili pro Váš nákup služeb " + invoice.corpoInfo.getName() + ". Vaše objednávka č. " + invoice.order.getOrder_number() + " objednaná ze dne " + invoice.order.getDate_order() + " byla vyzvednuta. \n" +
                    "V příloze naleznete elektronickou fakturu vystavenou na Vaše jméno. \n\n" +
                    "Toto je automaticky generovaný e-mail. Na tuto zprávu prosím neodpovídejte.\n\n" +
                    "S podzravem\n" +
                    ""+ invoice.corpoInfo.getName()+"");

            MimeBodyPart attach = new MimeBodyPart();
            attach.attachFile(file);
            multipart.addBodyPart(body);
            multipart.addBodyPart(attach);

            mimeMessage.setContent(multipart);


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




        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast toast = Toast.makeText(context,"Email has been sent",Toast.LENGTH_LONG);
        toast.show();
    }



}
