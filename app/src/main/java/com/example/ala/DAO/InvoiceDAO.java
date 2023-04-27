package com.example.ala.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class InvoiceDAO {
    private DatabaseReference databaseReference;
    public InvoiceDAO()
    {
       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Invoice").child("serial_number");
    }
    public Query get(String key)
    {
            return databaseReference.orderByKey();
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
