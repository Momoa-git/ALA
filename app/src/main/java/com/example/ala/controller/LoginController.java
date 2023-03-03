package com.example.ala.controller;

import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.ala.LoginActivity;
import com.example.ala.model.LoginModel;

public class LoginController {
    private LoginActivity view;
    private LoginModel model;

    public LoginController(LoginActivity view){
        this.view = view;
        model = new LoginModel(this);
    }

    public void loginOffice(){

        String email = view.edT_email.getText().toString().trim();
        String password = view.edT_password.getText().toString().trim();

        if (!checkErrors(email, password)){

            model.firebaseAuthenticationLogin(email, password);
        }

    }

    private Boolean checkErrors(String email, String password){

        if (email.isEmpty()){
            view.edT_email.setError("Prázdné pole");
            view.edT_email.requestFocus();
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            view.edT_email.setError("Nevalidni email");
            view.edT_email.requestFocus();
            return true;
        }

        if (password.isEmpty()){
            view.edT_password.setError("CPrázdné pole");
            view.edT_password.requestFocus();
            return true;
        }

        if (password.length() < 6){
            view.edT_password.setError("Heslo musí mít aspoň 6 znaků");
            view.edT_password.requestFocus();
            return true;
        }

        else {
            return false;
        }


    }


    public void startMenuActivity(){
        view.openMenuActivity();
    }

    public void setVisible(){
        view.progressBar.setVisibility(View.VISIBLE);
    }

    public void setUnVisible(){
        view.progressBar.setVisibility(View.GONE);
    }

    public void toastMsg() {
        Toast.makeText(view, "Failed to login", Toast.LENGTH_LONG).show();
    }
}
