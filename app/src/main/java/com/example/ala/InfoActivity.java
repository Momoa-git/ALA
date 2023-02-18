package com.example.ala;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InfoActivity extends AppCompatActivity {

    EditText edT_name_office, edT_email, edT_password, edT_address;
    Button btn_change;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        edT_name_office = findViewById(R.id.edT_name_office);
        edT_address = findViewById(R.id.edT_address);
        edT_email = findViewById(R.id.edT_email);
        edT_password = findViewById(R.id.edT_password);
        btn_change = findViewById(R.id.btn_change);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String address = getIntent().getStringExtra("address");


        edT_name_office.setText(name);
        edT_address.setText(address);
        edT_email.setText(email);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!email.equals(edT_email.getText().toString())) {
                    firebaseUser.updateEmail(edT_email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(InfoActivity.this, "Email is changed.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                    firebaseUser.updatePassword(edT_email.getText().toString());


            }
        });


    }
}
