package com.vsb.ala.DAO;

import com.vsb.ala.model.object.Office;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

public interface OfficeDAOInterface {
    Task<Void> addOffice(Office office);
    Task<Void> updateEmail(String key);
    Task<Void> updateName(String key);
    Task<Void> updateAddress(String key);
    Query getRef(String key);
    FirebaseUser getFirebUser();
}
