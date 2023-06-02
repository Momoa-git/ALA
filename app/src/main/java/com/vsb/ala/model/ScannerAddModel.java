package com.vsb.ala.model;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.vsb.ala.controller.ScannerAddController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScannerAddModel {

    ArrayList<String> arrayList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static final String PREFERENCE_NAME = "MyPref";

    private ScannerAddController controller;


    public ScannerAddModel(ScannerAddController controller) {
        this.controller = controller;
    }


    public void getProductByCode(String bar_code) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Product").child("Products");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String code = dataSnapshot.child("bar-code").getValue().toString();
                    if (bar_code.equals(code)){


                        String name = dataSnapshot.child("name").getValue().toString();
                        String price = dataSnapshot.child("price").getValue().toString();
                        int id = Integer.valueOf( dataSnapshot.child("id").getValue().toString());



                        controller.onNewProductDetail(name, price, code);


                        SharedPreferences.Editor myEdit = controller.preferences.edit();

                        myEdit.putInt("sp_id", id);
                        myEdit.commit();

                        controller.setVisibility();

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}
