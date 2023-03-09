package com.example.ala.model.object;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.ala.DAO.CorpoInfoDAO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;

import java.io.File;
import java.io.IOException;

public class InvoiceHeader {

    private static InvoiceHeader instance;
    String name, residence, ic, dic, website, contact, phone, bank_account, variable_symbol, logo_path;
    Bitmap logo_bitmap;
    CorpoInfoDAO corpoInfoDAO;

    private InvoiceHeader()
    {

    }

    public static InvoiceHeader getInstance(){
        if(instance == null){
            instance = new InvoiceHeader();
        }
        return instance;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public void setLogo_bitmap(Bitmap logo_bitmap) {
        this.logo_bitmap = logo_bitmap;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setResidence(String residence) {
        this.residence = residence;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public void setVariable_symbol(String variable_symbol) {
        this.variable_symbol = variable_symbol;
    }

    public String getName() {
        return name;
    }

    public String getResidence() {
        return residence;
    }

    public String getIc() {
        return ic;
    }

    public String getDic() {
        return dic;
    }

    public String getWebsite() {
        return website;
    }

    public String getContact() {
        return contact;
    }

    public String getPhone() {
        return phone;
    }

    public String getBank_account() {
        return bank_account;
    }

    public String getVariable_symbol() {
        return variable_symbol;
    }

    public Bitmap getLogo_bitmap() {
        return logo_bitmap;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public void initDao(){
        this.corpoInfoDAO = new CorpoInfoDAO();

    }

    public void fetchCorpoData(Invoice.FirebaseCallback3 firebaseCallback3){
       // CorpoInfoDAO corpoInfoDAO = new CorpoInfoDAO();
        corpoInfoDAO.get().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setName(snapshot.child("name").getValue().toString());
                setResidence(snapshot.child("residence").getValue().toString());
                setIc(snapshot.child("ič").getValue().toString());
                setDic(snapshot.child("dič").getValue().toString());
                setWebsite(snapshot.child("website").getValue().toString());
                setContact(snapshot.child("contact").getValue().toString());
                setPhone(snapshot.child("phone").getValue().toString());
                setBank_account(snapshot.child("bank account").getValue().toString());
                setVariable_symbol(snapshot.child("variable symbol").getValue().toString());
                setLogo_path(snapshot.child("logo").getValue().toString());

                firebaseCallback3.onCallBack3();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }



    public void fetchLogoImg(Invoice.FirebaseCallback firebaseCallback) {
      //  CorpoInfoDAO corpoInfoDAO = new CorpoInfoDAO();
       // readCorpoData(corpoInfoDAO);


        try {
            File localfile = File.createTempFile("tempfile",".jpg");
            corpoInfoDAO.getStorageRef(getLogo_path()).getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            setLogo_bitmap(bitmap);
                            firebaseCallback.onCallBack();



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void readCorpoData(CorpoInfoDAO corpoInfoDAO) {


        corpoInfoDAO.get().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setName(snapshot.child("name").getValue().toString());
                setResidence(snapshot.child("residence").getValue().toString());
                setIc(snapshot.child("ič").getValue().toString());
                setDic(snapshot.child("dič").getValue().toString());
                setWebsite(snapshot.child("website").getValue().toString());
                setContact(snapshot.child("contact").getValue().toString());
                setPhone(snapshot.child("phone").getValue().toString());
                setBank_account(snapshot.child("bank account").getValue().toString());
                setVariable_symbol(snapshot.child("variable symbol").getValue().toString());
                setLogo_path(snapshot.child("logo").getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    /*private interface FirebaseCallback{
        void onCallBack(Bitmap bitmap);
    }*/

}
