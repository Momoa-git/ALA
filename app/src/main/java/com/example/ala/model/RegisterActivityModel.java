package com.example.ala.model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ala.DAO.OfficeDAO;
import com.example.ala.DAO.OfficeDAOInterface;
import com.example.ala.model.object.Office;
import com.example.ala.controller.RegisterActivityController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivityModel extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private  RegisterActivityController controller;

    public RegisterActivityModel(RegisterActivityController controller) {
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
