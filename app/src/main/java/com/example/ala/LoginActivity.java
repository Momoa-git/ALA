package com.example.ala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ala.controller.LoginController;
import com.example.ala.view.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private LoginController controller;

    private Button btn_login;
    public EditText edT_email, edT_password;
    private TextView btn_register_activity;

    public ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new LoginController(this);

        btn_register_activity = findViewById(R.id.btn_register_activity);
        btn_login = findViewById(R.id.btn_login);
        edT_email = findViewById(R.id.edT_email);
        edT_password = findViewById(R.id.edT_password);

        //mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progress_bar);

        btn_register_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               controller.loginOffice();
            }
        });




    }






    public void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    public void openMenuActivity(){
        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
    }


}