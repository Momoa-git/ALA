package com.vsb.ala.DAO;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CorpoInfoDAO {
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    public CorpoInfoDAO()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Corporate info");
    }

    public Query get(String key)
    {
        return databaseReference.orderByKey();
    }

    public Query get()
    {
        return databaseReference;
    }

    public StorageReference getStorageRef(String param)
    {
        storageReference = FirebaseStorage.getInstance().getReference(param);

        return storageReference;
    }

}
