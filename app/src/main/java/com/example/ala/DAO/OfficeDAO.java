package com.example.ala.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class OfficeDAO {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    public OfficeDAO()
    {
        mAuth = FirebaseAuth.getInstance();
       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Office").child(mAuth.getUid()).child("Product");
    }
    public Task<Void> update(String key, HashMap<String ,Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> removeItem(String key)
    {
        return databaseReference.child(key).removeValue();
    }

    public Query get(String key)
    {
     //   if(key == null)
       // {
            return databaseReference.orderByKey();
      //  }
       // return databaseReference.orderByKey().startAfter(key).limitToFirst(8);
    }

    public void setSerial_number(int serial_number)
    {
        databaseReference.setValue(serial_number);
    }

    public Query get()
    {
        return databaseReference;
    }

}
