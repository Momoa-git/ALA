package com.example.ala.DAO;

import com.example.ala.model.object.Product;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

public interface ProductDAOInterface {
    Task<Void> removeItem(String key);
    Task<Void> setProduct(Product product);
    void setSerial_number(int serial_number);
    Query getSerialNumber();
    Query get();
    FirebaseAuth getmAuth();
}
