package com.example.ala;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerAddActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    ZXingScannerView scannerView;

    ArrayList<String> arrayList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

    }

    @Override
    public void handleResult(Result result) {


        NewProductActivity.edT_ks.setText("");
        NewProductActivity.edT_bar.setText(result.getText());


        getProductByCode(result.getText());



        onBackPressed();

    }

    private void getProductByCode(String bar_code) {



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Product").child("Products");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //  String value = dataSnapshot.child("value").getValue(String.class);
                    //  Product product = snapshot.getValue(Product.class);

                    String code = dataSnapshot.child("bar-code").getValue().toString();
                    if (bar_code.equals(code)){


                        String name = dataSnapshot.child("name").getValue().toString();
                        String price = dataSnapshot.child("price").getValue().toString();
                        int id = Integer.valueOf( dataSnapshot.child("id").getValue().toString());
                        NewProductActivity.edT_name_product.setText(name);
                        NewProductActivity.edT_price.setText(price + ".00 Kč");
                        NewProductActivity.txt_id.setText(id);

                        NewProductActivity.edT_ks.setVisibility(View.VISIBLE);
                        NewProductActivity.edT_line.setVisibility(View.VISIBLE);
                        NewProductActivity.edT_place.setVisibility(View.VISIBLE);
                        NewProductActivity.btn_add_item.setVisibility(View.VISIBLE);

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
        finish();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }


}
