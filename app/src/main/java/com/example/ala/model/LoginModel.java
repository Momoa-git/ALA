package com.example.ala.model;

import androidx.annotation.NonNull;

import com.example.ala.controller.LoginController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginModel {
    private LoginController controller;
    private FirebaseAuth mAuth;

    public LoginModel(LoginController controller){
        this.controller = controller;
    }

    public void firebaseAuthenticationLogin(String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        controller.setVisible();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //redirect to office profile
                           controller.startMenuActivity();
                            controller.setUnVisible();

                        }else {
                            controller.toastMsg();
                            controller.setUnVisible();
                        }
                    }
                });

    }
}
