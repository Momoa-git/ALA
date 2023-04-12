package com.example.ala.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ala.R;
import com.example.ala.controller.RegisterController;
import com.example.ala.service.InternetService;
import com.example.ala.view.dialog.InternetErrorDialog;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private RegisterController controller;

    private FirebaseAuth mAuth;
    public EditText edT_name_office, edT_place, edT_email, edT_password;
    public ProgressBar progressBar;
    private Button btn_register;
    private TextView btn_login_acitivity;
    private InternetService service;
    //TODO dopsat Toast msg

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_register);

        controller = new RegisterController(this);
        service = new InternetService(this);
       // mAuth = FirebaseAuth.getInstance();

        edT_name_office = findViewById(R.id.edT_name_office);
        edT_place = findViewById(R.id.edT_place);
        edT_email = findViewById(R.id.edT_email);
        edT_password = findViewById(R.id.edT_password);
        progressBar = findViewById(R.id.progress_bar);
        btn_register = findViewById(R.id.btn_register);
        btn_login_acitivity = findViewById(R.id.btn_login_activity);



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!service.checkConnection()){
                    InternetErrorDialog dialog = new InternetErrorDialog();
                    dialog.show(getSupportFragmentManager(),"internet error dialog");
                }
                else
                controller.registerOffice();
            }
        });


        btn_login_acitivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
}
