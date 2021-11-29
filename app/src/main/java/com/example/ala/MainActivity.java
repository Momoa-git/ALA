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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText edT_email, edT_password;
    private TextView btn_register_activity;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_register_activity = findViewById(R.id.btn_register_activity);
        btn_login = findViewById(R.id.btn_login);
        edT_email = findViewById(R.id.edT_email);
        edT_password = findViewById(R.id.edT_password);

        mAuth = FirebaseAuth.getInstance();

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
               loginOffice();
            }
        });





    }

    private void loginOffice(){

        String email = edT_email.getText().toString().trim();
        String password = edT_password.getText().toString().trim();

        if (!checkErrors(email, password)){

            firebaseAuthenticationLogin(email, password);
        }

    }

    private Boolean checkErrors(String email, String password){

        if (email.isEmpty()){
            edT_email.setError("Chyba emailu");
            edT_email.requestFocus();
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edT_email.setError("Nevalidni email");
            edT_email.requestFocus();
            return true;
        }

        if (password.isEmpty()){
            edT_password.setError("Chyba hesla");
            edT_password.requestFocus();
            return true;
        }

        if (password.length() < 6){
            edT_password.setError("aspon 6 znaku");
            edT_password.requestFocus();
            return true;
        }

        else {
            return false;
        }


    }


    private void firebaseAuthenticationLogin(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //redirect to office profile
                            startActivity(new Intent(MainActivity.this, MenuActivity.class));
                            progressBar.setVisibility(View.GONE);

                        }else {
                            Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }



    public void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }


}