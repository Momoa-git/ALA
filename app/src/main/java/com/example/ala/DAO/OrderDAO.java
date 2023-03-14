package com.example.ala.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class OrderDAO implements OrderDAOInterface{
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    public OrderDAO()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Order").child("Orders");
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

    public Task<Void> setStatus(int id, String status)
    {
        return databaseReference.child(String.valueOf(id - 1)).child("status").setValue(status);
    }

    public Task setPayDetails(int id, int old_sale, float result_price)
    {
        Map updatepay = new HashMap();
        updatepay.put("discount", old_sale);
        updatepay.put("price", result_price);


       /* invoice.setDiscount(old_sale);
        invoice.setResult_price(result_price+"");*/
        //firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Order").child("Orders").child(String.valueOf(id - 1)).child("Payment");

        return databaseReference.updateChildren(updatepay);
    }

    public Task setPayAfterPay(int id, String date, String time)
    {
        Map updatepay = new HashMap();

        updatepay.put("date_pay", date);
        updatepay.put("time_pay", time);
        updatepay.put("paid", true);

        databaseReference = firebaseDatabase.getReference().child("Order").child("Orders").child(String.valueOf(id - 1)).child("Payment");

        return databaseReference.updateChildren(updatepay);
    }

    public Query get()
    {
        return databaseReference;
    }

}
