package com.example.ala.controller;

import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.ala.view.RegisterActivity;
import com.example.ala.model.RegisterModel;

public class RegisterController {
    private RegisterModel model;
    private  RegisterActivity view;

    public RegisterController(RegisterActivity view){
        this.view = view;
        model = new RegisterModel(this);
    }

    public void registerOffice(){

        String name_office = view.edT_name_office.getText().toString().trim();
        String address = view.edT_place.getText().toString().trim();
        String email = view.edT_email.getText().toString().trim();
        String password = view.edT_password.getText().toString().trim();

        if (!checkErrors(name_office, address, email, password)){

            model.firebaseAuthenticationRegister(email, password, name_office, address);
        }

    }

    public void setVisibility()
    {
        view.progressBar.setVisibility(View.VISIBLE);
    }

    public void setInVisibility()
    {
        view.progressBar.setVisibility(View.GONE);
    }

    private Boolean checkErrors(String name_office, String address, String email, String password){

        if (name_office.isEmpty()){
            view.edT_name_office.setError("Chyba nazvu");
            view.edT_name_office.requestFocus();
            return true;
        }

        if (address.isEmpty()){
            view.edT_place.setError("Chyba adresy");
            view.edT_place.requestFocus();
            return true;
        }

        if (email.isEmpty()){
            view.edT_email.setError("Chyba emailu");
            view. edT_email.requestFocus();
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            view.edT_email.setError("Nevalidni email");
            view. edT_email.requestFocus();
            return true;
        }

        if (password.isEmpty()){
            view. edT_password.setError("Chyba hesla");
            view.  edT_password.requestFocus();
            return true;
        }

        if (password.length() < 6){
            view.  edT_password.setError("aspon 6 znaku");
            view. edT_password.requestFocus();
            return true;
        }

        else {
            return false;
        }


    }

    public void openIntent()
    {
        view.openLoginActivity();
    }

    public void displayToast(int i)
    {
        switch (i) {
            case 1:
                Toast.makeText(view, "Office is register!", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(view, "Failed registration2!", Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(view, "Failed registration. Office was already registred!", Toast.LENGTH_LONG).show();
                break;

        }
    }
}