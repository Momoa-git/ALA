package com.example.ala.DAO;

import com.example.ala.model.object.Product;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ProductDAO implements ProductDAOInterface{
    private DatabaseReference databaseReference, databaseReference2;
    private FirebaseAuth mAuth;

    public ProductDAO()
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Office").child(mAuth.getUid());
        databaseReference2 = firebaseDatabase.getReference().child("Product").child("register_number");
    }

    public Task<Void> setProduct(Product product)
    {
        return databaseReference.child("Product").push().setValue(product);
    }

    public void setSerial_number(int serial_number)
    {
        databaseReference2.setValue(serial_number);
    }

    public Query getSerialNumber()
    {
        return databaseReference2;
    }

    public Task<Void> removeItem(String key)
    {
        return databaseReference.child("Product").child(key).removeValue();
    }

    public Query get()
    {
        return databaseReference.child("Product");
    }

    public FirebaseAuth getmAuth(){
        return mAuth;
    }

}
