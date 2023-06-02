package com.vsb.ala.DAO;

import com.vsb.ala.model.object.Order;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;

public interface OrderDAOInterface {
    Query get();
    Task<Void> setStatus(int id, String status);
    Task setPayDetails(int id, int old_sale, float result_price);
    Task setPayAfterPay(int id, String date, String time);
    Task<Void> setRegisterNumberOfProduct(Order order, int product_iteration, int register_number);
    Query getProductItem(int id);
}
