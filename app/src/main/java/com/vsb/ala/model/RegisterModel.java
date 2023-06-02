package com.vsb.ala.model;

import androidx.annotation.NonNull;

import com.vsb.ala.DAO.OfficeDAO;
import com.vsb.ala.DAO.OfficeDAOInterface;
import com.vsb.ala.model.object.Office;
import com.vsb.ala.controller.RegisterController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterModel {

    private FirebaseAuth mAuth;
    private RegisterController controller;

    public RegisterModel(RegisterController controller) {
        this.controller = controller;
    }

    public void firebaseAuthenticationRegister(String email, String password, String name_office, String address) {
        mAuth = FirebaseAuth.getInstance();
        controller.setVisibility();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){   //if office is register
                            Office office = new Office(name_office, address, email);

                            OfficeDAOInterface officeDAO = new OfficeDAO();
                            officeDAO.addOffice(office).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){   //if data is in real database
                                        controller.displayToast(1);
                                        controller.setInVisibility();
                                        controller.openIntent();
                                    }
                                    else {
                                        controller.displayToast(2);
                                        controller.setInVisibility();
                                    }
                                }
                            });
                        }
                        else {
                            controller.displayToast(3);
                            controller.setInVisibility();

                        }
                    }
                });
    }


}
