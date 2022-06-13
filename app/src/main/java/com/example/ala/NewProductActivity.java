package com.example.ala;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ala.model.OrderActivityModel;
import com.example.ala.view.ScannerAddActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NewProductActivity extends AppCompatActivity {

    public static EditText edT_name_product, edT_price, edT_bar, edT_ks, edT_line, edT_place;
    private Button btn_scan;
    public static Button btn_add_item;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase, firebaseDatabase2, firebaseDatabase3;
    DatabaseReference databaseReference, databaseReference2, databaseReference3;
    public static TextView txt_id;
  // public int id;
    Integer piece;
    Integer reg_number, register_number;
    Integer semaphore;
    Integer counter;
    boolean order_assigned = false;

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
       // txt_id = findViewById(R.id.id);

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

             //   searchOrder();
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

                firebaseDatabase2 = FirebaseDatabase.getInstance();
                databaseReference2 = firebaseDatabase2.getReference().child("Product").child("register_number");

                piece = Integer.valueOf(edT_ks.getText().toString());

                String name = edT_name_product.getText().toString();
                String price = edT_price.getText().toString();
                String bar_code = edT_bar.getText().toString();
                String line = edT_line.getText().toString();
                String place = edT_place.getText().toString();

                SharedPreferences sh = getSharedPreferences("MyPref", MODE_PRIVATE);


                int id_list_product = sh.getInt("sp_id",0);

                Boolean valid_input = checkValidValues(piece, line, place);

                if(valid_input) {
                    Log.i("outline","[LIST:] ID: "+ id_list_product  + " Name: " + name + " Pieces: "+ piece + "x");
                  //  Product product = new Product(id,name, price, bar_code, line, place);
                    order_assigned = false;
                    //  databaseReference.child(String.valueOf(id+1)).setValue(product);
                    for (int i = 0; i < piece; i++) {
                        counter = 0;
                        readRegisterNumberData(new FirebaseCallback() {
                            @Override
                            public void onCallBack(int reg_num) {
                                register_number = reg_num + counter;
                                searchingOrder(id_list_product, new FirebaseCallback2() {
                                    @Override
                                    public void onCallBack2(boolean order_assigned) {
                                        Product product = new Product(register_number, id_list_product, name, price, bar_code, line, place, getActualDateTime(), order_assigned);
                                        databaseReference.push().setValue(product);
                                        int new_reg_number = register_number + 1;
                                        databaseReference2.setValue(new_reg_number);
                                        counter++;
                                        Log.i("outline","[WAREHOUSE:] Register_num.: " + register_number +" ID_list_product: "+ id_list_product  + " Name: " + name + " Price: "+ price +
                                                " BarCode: " + bar_code + " Line-place: " + line + "-" + place + " DateTime arrivals " + getActualDateTime() + " Assigned: " + order_assigned);
                                    }
                                });
                               /*
                                Product product = new Product(register_number, id_list_product, name, price, bar_code, line, place);
                                databaseReference.push().setValue(product);
                                int new_reg_number = register_number + 1;
                                databaseReference2.setValue(new_reg_number);
                                counter++;
                             //  databaseReference2.setValue(id + 1);
                                Log.i("outline","[WAREHOUSE:] Register_num.: " + register_number +" ID_list_product: "+ id_list_product  + " Name: " + name + " Price: "+ price + " BarCode: " + bar_code + " Line-place: " + line + "-" + place);
                           */ }
                        });


                    }
                    Toast.makeText(NewProductActivity.this, piece + " Product(s) added!" , Toast.LENGTH_LONG).show();

                }

            }

            private String getActualDateTime() {

                SimpleDateFormat output_format = new SimpleDateFormat("dd.M.yyyy H:mm:ss");
                Date date = new Date();

                return output_format.format(date);

            }

            private void searchingOrder(int id_list_product, FirebaseCallback2 firebaseCallback) {

                Log.i("outline", "Before searching");
                firebaseDatabase3 = FirebaseDatabase.getInstance();
                databaseReference3 = firebaseDatabase3.getReference().child("Order").child("Orders");

                databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Order order = dataSnapshot.getValue(Order.class);

                            String[] str = order.getId_list_product().split(",");
                            String[] array_id_list_product = new String[str.length];
                            for(int i = 0; i < str.length; i++)
                                array_id_list_product[i] = str[i];

                            for(int j = 0; j < array_id_list_product.length; j++) {
                                if (mAuth.getUid().contains(order.getOffice()) && array_id_list_product[j].equals(String.valueOf(id_list_product - 1)) && order.getStatus().equals("PE")) {
                                    Log.i("outline", "[MATCH IN ORDER:]" + String.valueOf(order.getOrder_number()) + " " +  array_id_list_product[j]);
                                   // updateOrder(dataSnapshot);
                                    order_assigned = true;
                                    break;
                                }
                            }

                        }
                        firebaseCallback.onCallBack2(order_assigned);
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });

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
/*
    private boolean searchOrder() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Order").child("Orders");
    }*/

    private void readRegisterNumberData(FirebaseCallback firebaseCallback){
        Log.i("outline", "Before read");
        semaphore = 0;
        firebaseDatabase2 = FirebaseDatabase.getInstance();
        databaseReference2 = firebaseDatabase2.getReference().child("Product").child("register_number");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reg_number = Integer.valueOf(String.valueOf(snapshot.getValue()));
                Log.i("outline", "Before call");
                    firebaseCallback.onCallBack(reg_number);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


    private interface FirebaseCallback{
       void onCallBack(int reg_num);
    }

    private interface FirebaseCallback2{
        void onCallBack2(boolean order_assigned);
    }

}


