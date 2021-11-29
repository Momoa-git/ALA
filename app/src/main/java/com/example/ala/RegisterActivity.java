package com.example.ala;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edT_name_office, edT_place, edT_email, edT_password;
    private ProgressBar progressBar;
    private Button btn_register;
    private TextView btn_login_acitivity;

    //TODO dopsat Toast msg

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

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
                registerOffice();
            }
        });


        btn_login_acitivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

    }

    private void registerOffice(){

        String name_office = edT_name_office.getText().toString().trim();
        String address = edT_place.getText().toString().trim();
        String email = edT_email.getText().toString().trim();
        String password = edT_password.getText().toString().trim();

        if (!checkErrors(name_office, address, email, password)){

            firebaseAuthenticationRegister(email, password, name_office, address);
        }

    }

    private Boolean checkErrors(String name_office, String address, String email, String password){

        if (name_office.isEmpty()){
            edT_name_office.setError("Chyba nazvu");
            edT_name_office.requestFocus();
            return true;
        }

        if (address.isEmpty()){
            edT_place.setError("Chyba adresy");
            edT_place.requestFocus();
            return true;
        }

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

    private void firebaseAuthenticationRegister(String email, String password, String name_office, String address) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){   //if office is register
                            Office office = new Office(name_office, address, email);

                            FirebaseDatabase.getInstance().getReference("Office")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(office).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){   //if data is in real database
                                        Toast.makeText(RegisterActivity.this, "Office is register!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        openLoginActivity();
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Failed registration2!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Failed registration1!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
