package com.vsb.ala.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.vsb.ala.controller.WarehouseController;
import com.vsb.ala.model.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WarehouseModel {
    private final Context context;
    private WarehouseController controller;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    ArrayList<String> list_barcode;

    public WarehouseModel(Context context, WarehouseController controller){
        this.context = context;
        this.controller = controller;
    }

    public void setRecViewContent(ArrayList<Product> list, ArrayList<Product> list2) {
        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser office = mAuth.getCurrentUser();
        String id = office.getUid();

        databaseReference =  FirebaseDatabase.getInstance().getReference().child("Office").child(id).child("Product");

        list_barcode = new ArrayList<>();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);

                    if (!list_barcode.contains(product.getBar_code())) {
                        list.add(product);
                    }


                    list_barcode.add(product.getBar_code());
                   list2.add(product);
                }

                controller.onNotifyDataSetChanged();
                controller.setInvisibilityProgressBar();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }


    public void getFirebaseResources(String bar_codeP) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Product").child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String bar_code = dataSnapshot.child("bar-code").getValue().toString();
                    if(bar_code.equals(bar_codeP)) {
                        //    int id = Integer.valueOf(dataSnapshot.child("id").getValue().toString());
                        String imageRes = dataSnapshot.child("image").getValue().toString();
                        String desc = dataSnapshot.child("description").getValue().toString();
                        Log.i("getFirebaseT", "" + imageRes + " " + desc);

                         SharedPreferences sharedpreferences;
                        sharedpreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedpreferences.edit();

                        myEdit.putString("fr_image", imageRes);
                        myEdit.putString("fr_desc", desc);
                        myEdit.commit();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
