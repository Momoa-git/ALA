 package com.vsb.ala.model.object;

import java.util.Observable;
import java.util.Observer;

 public class PaymentDetailSender extends Sender implements Observer {

     Payment payment;

     public PaymentDetailSender(Payment payment){
        this.payment = payment;

     }

     public void sendPaymentDetailToEmail()
     {

         setProperties();
         setSession();
         setRecieverEmail(payment.order.getCustomer_email());
         setSubjectEmail("Informace o objednávce " + payment.order.getOrder_number());
         setBodyEmail("Vážený zákazníku, \n" +
                 "děkujeme, za Váš nákup ve výši " + payment.getPrice() + " Kč, objednávku jsme v pořádku přijali. O jejím zpracování Vás budeme dále informovat e-mailem. \n" +
                 "Toto je automaticky generovaný e-mail. Na tuto zprávu prosím neodpovídejte.\n\n" +
                 "S pozdravem\n" +
                 "Smile Shop s.r.o.");
         sendEmail();
     }


     @Override
     public void update(Observable o, Object arg) {
         sendPaymentDetailToEmail();
     }
 }
