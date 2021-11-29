package com.example.ala;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewProductActivity extends AppCompatActivity {

    public static EditText edT_name_product, edT_price, edT_bar, edT_ks, edT_line, edT_place;
    private Button btn_scan;
    public static Button btn_add_item;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public static TextView txt_id;
  // public int id;
    Integer piece;

    //TODO ošetřit, když dostanu nevalidní ean kod
    //TODO ošetřit, když počet kusů nebo umístění bude null
    //TODO nastavit modrou listu na velikost 30% obrazovky

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        edT_name_product = findViewById(R.id.edT_name_product);
        edT_bar = findViewById(R.id.edT_bar);
        btn_scan = findViewById(R.id.btn_scan);
        edT_name_product = findViewById(R.id.edT_name_product);
        edT_price= findViewById(R.id.edT_price);
        btn_add_item = findViewById(R.id.btn_add_item);
        edT_ks = findViewById(R.id.edT_ks);
        edT_line = findViewById(R.id.edT_line);
        edT_place = findViewById(R.id.edT_place);
        txt_id = findViewById(R.id.id);

        mAuth = FirebaseAuth.getInstance();



        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(getApplicationContext(), ScannerAddActivity.class));
                startActivity(new Intent(NewProductActivity.this, ScannerAddActivity.class));


            }
        });



        btn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                addNewProduct();
                fillEmptyAll();


            }

            private void fillEmptyAll() {

                edT_name_product.setText("");
                edT_price.setText("");
                edT_bar.setText("");
                edT_ks.setVisibility(View.INVISIBLE);
                edT_line.setVisibility(View.INVISIBLE);
                edT_place.setVisibility(View.INVISIBLE);
                btn_add_item.setVisibility(View.INVISIBLE);


            }

            private void addNewProduct() {
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("Office").child(mAuth.getUid()).child("Product");

                piece = Integer.valueOf(edT_ks.getText().toString());

                String name = edT_name_product.getText().toString();
                String price = edT_price.getText().toString();
                String bar_code = edT_bar.getText().toString();
                String line = edT_line.getText().toString();
                String place = edT_place.getText().toString();
                int id = Integer.valueOf(txt_id.getText().toString());

                Boolean valid_input = checkValidValues(piece, line, place);

                if(valid_input) {

                    Product product = new Product(id,name, price, bar_code, line, place);

                    //  databaseReference.child(String.valueOf(id+1)).setValue(product);
                    for (int i = 0; i < piece; i++) {
                        databaseReference.push().setValue(product);
                    }
                    Toast.makeText(NewProductActivity.this, "Product was added!" + id, Toast.LENGTH_LONG).show();

                }

            }

            private Boolean checkValidValues(int piece, String line, String place) {

                if (piece == 0 || Integer.valueOf(line) == 0 || Integer.valueOf(place) == 0)
                {
                    Toast.makeText(NewProductActivity.this, "Hodnoty se nesmí rovnat 0.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (edT_ks.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(NewProductActivity.this, "Pole musí být vyplněné.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    return true;
                }

            }


        });


    }



    }


