package com.example.ala.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class OrderDAO {
    private DatabaseReference databaseReference;
    public OrderDAO()
    {
       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Order").child("Orders");
    }
    public Task<Void> update(String key, HashMap<String ,Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Query get(String key)
    {
     //   if(key == null)
       // {
            return databaseReference.orderByKey();
      //  }
       // return databaseReference.orderByKey().startAfter(key).limitToFirst(8);
    }

    public Query get()
    {
        return databaseReference;
    }

}
