package com.example.ala.DAO;

import com.example.ala.model.object.Office;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class OfficeDAO implements OfficeDAOInterface{
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    public Office office;
    public OfficeDAO()
    {
        mAuth = FirebaseAuth.getInstance();
       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Office").child(mAuth.getUid());
    }
    public Task<Void> updateEmail(String key)
    {
        return databaseReference.child("email").setValue(key);
    }

    public Task<Void> updateName(String key)
    {
        return databaseReference.child("name").setValue(key);
    }

    public Task<Void> updateAddress(String key)
    {
        return databaseReference.child("address").setValue(key);
    }

    public Task<Void> addOffice(Office office){
        return databaseReference.setValue(office);
    }

    public Task<Void> removeItem(String key)
    {
        return databaseReference.child("Product").child(key).removeValue();
    }


    public Query get(String key)
    {
     //   if(key == null)
       // {
            return databaseReference.child("Product").orderByKey();
      //  }
       // return databaseReference.orderByKey().startAfter(key).limitToFirst(8);
    }

    public void setSerial_number(int serial_number)
    {
        databaseReference.child("Product").setValue(serial_number);
    }

    public Query get()
    {
        return databaseReference.child("Product");
    }

}
