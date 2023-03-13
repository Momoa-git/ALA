package com.example.ala.DAO;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;

public interface ProductDAOInterface {
    Task<Void> removeItem(String key);
    Query get();
}
