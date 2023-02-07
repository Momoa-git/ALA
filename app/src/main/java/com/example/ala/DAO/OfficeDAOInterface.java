package com.example.ala.DAO;

import com.example.ala.model.object.Office;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

public interface OfficeDAOInterface {
    Task<Void> addOffice(Office office);
}
