package com.example.ala.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;

public interface OrderDAOInterface {
    Query get();
    Task<Void> setStatus(int id, String status);
    Task setPayDetails(int id, int old_sale, float result_price);
    Task setPayAfterPay(int id, String date, String time);
}
