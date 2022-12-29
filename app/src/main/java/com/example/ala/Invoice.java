package com.example.ala;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ala.DAO.InvoiceDAO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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


public class Invoice {
    String datetime;
    int serial_number;
    final int DPH_percent = 21;
    Order order;
    Context context;
    InvoiceCorpoInfo corpoInfo;



    public Invoice(Order order, Context context) {
        this.order = order;
        this.context = context;
        initHeaderData();

    }



    private void initHeaderData() {
        corpoInfo = InvoiceCorpoInfo.getInstance();
        corpoInfo.initDao();
        corpoInfo.fetchCorpoData(new Invoice.FirebaseCallback3(){
            @Override
            public void onCallBack3() {

                corpoInfo.fetchLogoImg(new Invoice.FirebaseCallback(){
                    @Override
                    public void onCallBack() {

                        checkAndSetSerialNumber();
                    }

                });
            }

        });
    }




    public String getDatetime() {
        return datetime;
    }

    public void setDatetime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        Date date = new Date();
        this.datetime = formatter.format(date);
    }

    public int getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }

    public void checkAndSetSerialNumber() {



                readRegisterNumberData(new FirebaseCallback2(){
                    @Override
                    public void onCallBack2(int serial_num, InvoiceDAO invoiceDAO, Context context) {

                        try {
                            setSerial_number(serial_num);
                            InvoiceTemplate template = new InvoiceTemplate(Invoice.this, context);
                            InvoiceSender sender = new InvoiceSender(Invoice.this, context);
                            sender.sendInvoiceToEmail(template.createPDF());
                            invoiceDAO.setSerial_number(serial_num + 1);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                });

    }

    private void readRegisterNumberData(FirebaseCallback2 firebaseCallback){
        Log.i("outline", "Before read");
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        invoiceDAO.get().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int serial_number = Integer.valueOf(String.valueOf(snapshot.getValue()));
                Log.i("outline", "Before call");
                firebaseCallback.onCallBack2(serial_number, invoiceDAO, context);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


    public float getDiscountAmount(long discount) {
       float original_price = 100 * Float.valueOf(order.getPrice()) / (100 - discount);
       Log.i("original_price", original_price + " ");
       return original_price * discount / 100 * (-1);
    }

    public double calculateSumPrice(double price, Long count) {
        return price * count;
    }

    public double calculatePriceWithoutDPH(double price, double dph) {
        return price - dph;
    }

    public double calculateDPH(double price) {
        return price * DPH_percent / 100;
    }

    public String setPriceFormat(String price) {
        Log.i("priceDPH", price );
        double amount = Double.parseDouble(price);
        DecimalFormat formater = new DecimalFormat("###,###.00");

        return formater.format(amount);
    }

    public String getActualDate() {
        SimpleDateFormat output_format = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();

        return  output_format.format(date);
    }


    public interface FirebaseCallback{
        void onCallBack();
    }

    public interface FirebaseCallback3{
        void onCallBack3();
    }

    private interface FirebaseCallback2{
        void onCallBack2(int serial_num, InvoiceDAO invoiceDAO, Context context);
    }

}
