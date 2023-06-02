 package com.vsb.ala.model.object;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

 public class InvoiceSender extends Sender{

    Invoice invoice;
    Context context;

    public InvoiceSender(Invoice invoice, Context context){
       this.invoice = invoice;
       this.context = context;
    }


    public void sendInvoiceToEmail(File file)
    {

        setProperties();
        setSession();
        setRecieverEmail(invoice.order.getCustomer_email());
        setSubjectEmail("Informace o vyřízení objednávky č. " + invoice.order.getOrder_number());
        setBodyEmail("Dobrý den, \n" +
                "děkujeme, že jste využili pro Váš nákup služeb " + invoice.corpoInfo.getName() + ". Vaše objednávka č. " + invoice.order.getOrder_number() + " objednaná ze dne " + invoice.order.getDate_order() + " byla vyzvednuta. \n" +
                "V příloze naleznete elektronickou fakturu vystavenou na Vaše jméno. \n\n" +
                "Toto je automaticky generovaný e-mail. Na tuto zprávu prosím neodpovídejte.\n\n" +
                "S pozdravem\n" +
                ""+ invoice.corpoInfo.getName()+"");
        addAttachFile(file);
        sendEmail();

        Toast toast = Toast.makeText(context,"Email has been sent",Toast.LENGTH_LONG);
        toast.show();
    }



}
