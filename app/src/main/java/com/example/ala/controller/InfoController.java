package com.example.ala.controller;

import android.util.Patterns;

import com.example.ala.view.InfoActivity;
import com.example.ala.model.InfoModel;

public class InfoController {
    private InfoActivity view;
    private InfoModel model;
    
    public InfoController(InfoActivity view){
        this.view = view;
        model = new InfoModel(this);
    }

    public void setTextFields(String name, String address, String email) {
        view.edT_name_office.setText(name);
        view.edT_address.setText(address);
        view.edT_email.setText(email);
    }

    public void updateInformation(String namePrimal, String addressPrimal, String emailPrimal) {
        String name = view.edT_name_office.getText().toString();
        String address = view.edT_address.getText().toString();
        String email = view.edT_email.getText().toString();
        String passwd = view.edT_password.getText().toString();

        if(!checkErrors(name,address, email, passwd))
        {
            model.updateData(namePrimal, addressPrimal, emailPrimal, name, address, email, passwd);
        }

    }

    private boolean checkErrors(String name, String address, String email, String passwd) {
        if (name.isEmpty()){
            view.edT_name_office.setError("Prázdné pole");
            view.edT_name_office.requestFocus();
            return true;
        }

        if (address.isEmpty()){
            view.edT_address.setError("Prázdné pole");
            view.edT_address.requestFocus();
            return true;
        }

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

        if (passwd.isEmpty()){
            view.edT_password.setError("Prázdné pole");
            view.edT_password.requestFocus();
            return true;
        }

        if (passwd.length() < 6){
            view.  edT_password.setError("Heslo musí mít aspoň 6 znaků");
            view. edT_password.requestFocus();
            return true;
        }

        else {
            return false;
        }
    }

    public void signOut() {
        view.signOut();
    }
}
