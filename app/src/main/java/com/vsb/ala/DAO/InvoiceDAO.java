package com.vsb.ala.DAO;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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
