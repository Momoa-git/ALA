package com.example.ala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.FtsOptions;

import android.os.Bundle;
import android.widget.Spinner;

import com.example.ala.Inventory.StatusData;

public class OrderActivity extends AppCompatActivity {

    private Spinner spinner_status;
    private StatusAdapter statAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        spinner_status = findViewById(R.id.spinner_filter);
        statAdapter = new StatusAdapter(OrderActivity.this, StatusData.getStatusList());
        spinner_status.setAdapter(statAdapter);

    }
}