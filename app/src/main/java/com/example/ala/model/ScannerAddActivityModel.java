package com.example.ala.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ala.NewProductActivity;
import com.example.ala.Preferences;
import com.example.ala.controller.ScannerAddActivityController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScannerAddActivityModel extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Context context;

    public static final String PREFERENCE_NAME = "MyPref";
   // SharedPreferences sharedPreferences;

    private ScannerAddActivityController controller;


    public ScannerAddActivityModel(ScannerAddActivityController controller) {
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
                    //  String value = dataSnapshot.child("value").getValue(String.class);
                    //  Product product = snapshot.getValue(Product.class);

                    String code = dataSnapshot.child("bar-code").getValue().toString();
                    if (bar_code.equals(code)){


                        String name = dataSnapshot.child("name").getValue().toString();
                        String price = dataSnapshot.child("price").getValue().toString();
                        int id = Integer.valueOf( dataSnapshot.child("id").getValue().toString());



                        controller.onNewProductDetail(name, price, code);

                      //  final SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = controller.preferences.edit();

                        myEdit.putInt("sp_id", id);
                        myEdit.commit();

                    //    Preferences preferences = new Preferences(context);
                     //   preferences.setID(id);

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
