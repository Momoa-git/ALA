package com.example.ala.DAO;

import com.example.ala.model.object.Order;
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

    public Query get(String key)
    {
            return databaseReference.orderByKey();
    }

    public Task<Void> setStatus(int id, String status)
    {
        return databaseReference.child(String.valueOf(id)).child("status").setValue(status);
    }

    public Task<Void> setRegisterNumberOfProduct(Order order, int product_iteration, int register_number)
    {
        return databaseReference.child(String.valueOf(order.getId_order() - 1)).child("Product item").child(product_iteration + "").child("registration_num").setValue(register_number);
    }

    public Task setPayDetails(int id, int old_sale, float result_price)
    {
        Map updatepay = new HashMap();
        updatepay.put("discount", old_sale);
        updatepay.put("price", result_price);

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

    public Query getProductItem(int id)
    {
        return databaseReference.child(id+"").child("Product item");
    }

}
