package com.example.ala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

//asdasdasdasdasd
public class DetailProductActivity extends AppCompatActivity {

    TextView txt_bar_code, txt_name, txt_price, txt_piece, txt_line, txt_place, txt_desc;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        txt_bar_code = findViewById(R.id.txt_bar_code);
        txt_name = findViewById(R.id.txt_name);
        txt_price = findViewById(R.id.txt_price);
        txt_piece = findViewById(R.id.txt_piece);
        txt_line = findViewById(R.id.txt_line);
        txt_place = findViewById(R.id.txt_place);
        image = findViewById(R.id.image);
        txt_desc = findViewById(R.id.txt_desc);


       Intent intent = getIntent();
       Bundle b = intent.getExtras();

        SharedPreferences sh = getSharedPreferences("MyPref", MODE_PRIVATE);

        String title = sh.getString("product_title","");
        String price = sh.getString("product_price","");
        String piece = sh.getString("product_piece","");
        String bar_code = sh.getString("product_bar_code","");
        String line = sh.getString("product_line","");
        String place = sh.getString("product_place","");
        String imageRes = sh.getString("fr_image","");
        String desc = sh.getString("fr_desc","");


        Log.i("getFirebase", "From Detail: " + imageRes);
        Picasso.get().load(imageRes).into(image);



       txt_name.setText(title);
       txt_price.setText(price);
       txt_piece.setText(piece);
       txt_bar_code.setText(bar_code);
       txt_line.setText(line);
       txt_place.setText(place);
       txt_desc.setText(desc);

    }
}