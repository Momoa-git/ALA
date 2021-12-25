package com.example.ala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
//asdasdasd
public class DetailProductActivity extends AppCompatActivity {

    TextView txt_bar_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        txt_bar_code = findViewById(R.id.txt_bar_code);

        String newString;

       Intent intent = getIntent();
       Bundle b = intent.getExtras();

       String j = (String) b.get("product");
        txt_bar_code.setText(j);

    }
}