package com.example.ala.model;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.ala.DAO.OfficeDAO;
import com.example.ala.DAO.OfficeDAOInterface;
import com.example.ala.InfoActivity;
import com.example.ala.MainActivity;
import com.example.ala.controller.InfoController;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InfoModel {
    private InfoController controller;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public InfoModel(InfoController controller){
        this.controller = controller;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void updateData(String namePrimal, String addressPrimal, String emailPrimal, String name, String address, String email, String passwd) {
        OfficeDAOInterface officeDAOInterface = new OfficeDAO();

        if(!emailPrimal.equals(email)) {
            firebaseUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    officeDAOInterface.updateEmail(email);
                    Log.i("passwd", "Email is changed");
                }
            });
        }
        if(!namePrimal.equals(name)){
            officeDAOInterface.updateName(name);
            Log.i("passwd", "Name is changed");
        }
        if(!addressPrimal.equals(address)){
            officeDAOInterface.updateAddress(address);
            Log.i("passwd", "Address is changed");
        }

        updatePasswrd(passwd);
    }

    private void updatePasswrd(String passwd) {
        firebaseUser.updatePassword(passwd);
        Log.i("passwd", "Password is changed");

        controller.signOut();

    }
}
