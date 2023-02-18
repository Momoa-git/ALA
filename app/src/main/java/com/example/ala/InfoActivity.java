package com.example.ala;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ala.DAO.OfficeDAO;
import com.example.ala.DAO.OfficeDAOInterface;
import com.example.ala.controller.InfoController;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InfoActivity extends AppCompatActivity {

    InfoController controller;

    public EditText edT_name_office, edT_email, edT_password, edT_address;
    Button btn_change;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        controller = new InfoController(this);


        edT_name_office = findViewById(R.id.edT_name_office);
        edT_address = findViewById(R.id.edT_address);
        edT_email = findViewById(R.id.edT_email);
        edT_password = findViewById(R.id.edT_password);
        btn_change = findViewById(R.id.btn_change);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String address = getIntent().getStringExtra("address");

        controller.setTextFields(name, address, email);


        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                controller.updateInformation(name, address, email);



            }
        });


    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(InfoActivity.this, MainActivity.class));
    }
}
