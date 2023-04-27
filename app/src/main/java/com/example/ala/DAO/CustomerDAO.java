package com.example.ala.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class CustomerDAO implements CustomerDAOInterface{
    private DatabaseReference databaseReference;
    public CustomerDAO()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Customer").child("Customers");
    }

    public Query get(String key)
    {
        return databaseReference.orderByKey();
    }

    public Query get()
    {
        return databaseReference;
    }
}
