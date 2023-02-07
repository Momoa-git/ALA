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

import com.example.ala.model.object.Order;
import com.example.ala.model.object.Product;
import com.example.ala.view.ScannerAddActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewProductActivity extends AppCompatActivity{

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
    boolean semaphore = true;
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

                firebaseDatabase3 = FirebaseDatabase.getInstance();
                databaseReference3 = firebaseDatabase2.getReference().child("Order").child("Orders");

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
                    counter = 0;

                        semaphore = false;



                            readRegisterNumberData(new FirebaseCallback() {
                                @Override
                                public void onCallBack(int reg_num) {
                                   searchingOrder(id_list_product, register_number, new FirebaseCallback2() {
                                        @Override
                                        public void onCallBack2(boolean order_assigned, Order order, int product_iteration) {
                                            for (int i = 0; i < piece; i++) {
                                                register_number = reg_num + counter;

                                                Product product = new Product(register_number, id_list_product, name, price, bar_code, line, place, getActualDateTime(), order_assigned);
                                                databaseReference.push().setValue(product);
                                                int new_reg_number = register_number + 1;
                                                databaseReference2.setValue(new_reg_number);
                                                counter++;

                                                if (order_assigned == true) {
                                                    Log.i("outline", "[MATCH:] Order: " + order.getOrder_number() + " | Product: " + register_number);
                                                    databaseReference3.child(String.valueOf(order.getId_order() - 1)).child("Product item").child(product_iteration + "").child("registration_num").setValue(register_number);

                                                    checkfillAllProduct(databaseReference3, order.getId_order() - 1, new FirebaseCallback3(){
                                                        @Override
                                                        public void onCallBack3() {

                                                        }

                                                    });

                                                    Log.i("outline", "[WAREHOUSE:] Register_num.: " + register_number + " ID_list_product: " + id_list_product + " Name: " + name + " Price: " + price +
                                                            " BarCode: " + bar_code + " Line-place: " + line + "-" + place + " DateTime arrivals " + getActualDateTime() + " Assigned: " + order_assigned);

                                                    order_assigned = false;
                                                }
                                                else{
                                                    Log.i("outline", "[WAREHOUSE:] Register_num.: " + register_number + " ID_list_product: " + id_list_product + " Name: " + name + " Price: " + price +
                                                            " BarCode: " + bar_code + " Line-place: " + line + "-" + place + " DateTime arrivals " + getActualDateTime() + " Assigned: " + order_assigned);
                                                }


                                            }
                                    }

                                });
                                   // }
                                }

                        });


                    Toast.makeText(NewProductActivity.this, piece + " Product(s) added!" , Toast.LENGTH_LONG).show();

                }

            }

            private String getActualDateTime() {

                SimpleDateFormat output_format = new SimpleDateFormat("dd.M.yyyy H:mm:ss");
                Date date = new Date();

                return output_format.format(date);

            }

            private void searchingOrder(int id_list_product, Integer register_number, FirebaseCallback2 firebaseCallback) {

                Log.i("outline", "Before searching");
                firebaseDatabase3 = FirebaseDatabase.getInstance();
                databaseReference3 = firebaseDatabase3.getReference().child("Order").child("Orders");

                databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Order searchOrder = null;
                        int product_iteration = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Order order = dataSnapshot.getValue(Order.class);
                            long count_product = Long.valueOf(dataSnapshot.child("Product item").getChildrenCount());
                            for (int i = 0; mAuth.getUid().contains(order.getOffice()) && i < count_product; i++)
                            {
                                Log.i("DBZ", "ORDER: "+ order.getOrder_number());
                                int id_product = Integer.valueOf(dataSnapshot.child("Product item").child(i+"").child("id_product").getValue().toString()) + 1;
                                int registration_num = Integer.valueOf(dataSnapshot.child("Product item").child(i+"").child("registration_num").getValue().toString());
                                Log.i("DBZ", "ID product " + id_product + " IDLISTPRODUCT: " + id_list_product + "|" + registration_num);


                                if(id_product == id_list_product && order.getStatus().equals("PE") && registration_num == 0) {

                                            Log.i("DBZ", "[MATCH IN ORDER:]" + id_list_product );
                                            order_assigned = true;
                                            searchOrder = order;
                                            product_iteration = i;

                                }

                            }


                        }
                        firebaseCallback.onCallBack2(order_assigned, searchOrder, product_iteration);
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

    private void checkfillAllProduct(DatabaseReference databaseReference, int id, FirebaseCallback3 firebaseCallback3) {


        firebaseDatabase3 = FirebaseDatabase.getInstance();
        databaseReference3 = firebaseDatabase3.getReference().child("Order").child("Orders").child(id+"").child("Product item");

        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  int i = 0;
                long count_product = Long.valueOf(snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProductInOrder product =  dataSnapshot.getValue(ProductInOrder.class);

                    i++;
                    if(product.getRegistration_num() == 0)
                        break;

                    Log.i("outline", "Fill reg. num: " + snapshot.child("registration_num").getValue() + " " + count_product);

                    if(i == count_product) {
                        databaseReference.child(String.valueOf(id)).child("status").setValue("IP");
                        Log.i("outline", "STATUS CHANGE...");
                    }
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        firebaseCallback3.onCallBack3();

    }


/*
    private boolean searchOrder() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Order").child("Orders");
    }*/

    private void readRegisterNumberData(FirebaseCallback firebaseCallback){
        Log.i("outline", "Before read");
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
        void onCallBack2(boolean order_assigned, Order order, int product_iteration);
    }
    private interface FirebaseCallback3{
        void onCallBack3();
    }

}


