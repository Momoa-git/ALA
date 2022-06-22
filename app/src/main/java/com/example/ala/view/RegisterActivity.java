package com.example.ala.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ala.MainActivity;
import com.example.ala.R;
import com.example.ala.controller.RegisterActivityController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private RegisterActivityController controller;

    private FirebaseAuth mAuth;
    public EditText edT_name_office, edT_place, edT_email, edT_password;
    public ProgressBar progressBar;
    private Button btn_register;
    private TextView btn_login_acitivity;

    //TODO dopsat Toast msg

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_register);

        controller = new RegisterActivityController(this);

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
