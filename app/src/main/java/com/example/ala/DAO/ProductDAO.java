package com.example.ala.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ProductDAO implements ProductDAOInterface{
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    public ProductDAO()
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Office").child(mAuth.getUid());
    }

    public Task<Void> removeItem(String key)
    {
        return databaseReference.child("Product").child(key).removeValue();
    }

    public Query get()
    {
        return databaseReference.child("Product");
    }
}
