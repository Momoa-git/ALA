package com.vsb.ala.DAO;

import com.vsb.ala.model.object.Office;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

public class OfficeDAO implements OfficeDAOInterface{
    private DatabaseReference databaseReference, databaseReference2;
    private FirebaseAuth mAuth;
    public Office office;
    public OfficeDAO()
    {
        mAuth = FirebaseAuth.getInstance();
       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Office").child(Objects.requireNonNull(mAuth.getUid()));
        databaseReference2 = firebaseDatabase.getReference().child("Office");
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


    public Query get(String key)
    {
            return databaseReference.child("Product").orderByKey();
    }


    public Query getRef(String key)
    {
        return databaseReference2.child(key);
    }

   public FirebaseUser getFirebUser(){
       FirebaseAuth mAuth;
       mAuth = FirebaseAuth.getInstance();

       final FirebaseUser office = mAuth.getCurrentUser();

       return office;
   }

}
