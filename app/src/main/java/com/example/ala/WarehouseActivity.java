package com.example.ala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ala.model.object.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class WarehouseActivity extends AppCompatActivity implements ProductAdapter.OnDetailListener {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ProgressBar progress_bar;
    private TextView txt_emptylist;
    ProductAdapter productAdapter;
    ArrayList<Product> list, list2;
    ArrayList<String> list_barcode;
    Integer count = 1;
   // Semaphore semaphore;

    //TODO opravit hlavičku layoutu - přizpůsobit background
    //TODO při žádných vypsat "Prázdný sklad"
    //TODO sleep(time) upravit na semafory

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);



        recyclerView = findViewById(R.id.recycler_view);
        progress_bar = findViewById(R.id.progress_bar);
        txt_emptylist = findViewById(R.id.txt_emptylist);

        mAuth = FirebaseAuth.getInstance();

         final FirebaseUser office = mAuth.getCurrentUser();
        String id = office.getUid();

      //  firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =  FirebaseDatabase.getInstance().getReference().child("Office").child(id).child("Product");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        list_barcode = new ArrayList<>();
        productAdapter = new ProductAdapter(this, list, list2, this);
        recyclerView.setAdapter(productAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                   


                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);





                        if (!list_barcode.contains(product.getBar_code())) {
                            list.add(product);
                        }


                        list_barcode.add(product.getBar_code());
                        list2.add(product);
                    }

                productAdapter.notifyDataSetChanged();
                progress_bar.setVisibility(View.GONE);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }

    @Override
    public void onDetailClick(int position) {
       // list.get(position);
      //  Toast.makeText(this, list.get(position).toString(), Toast.LENGTH_SHORT).show();
        progress_bar.setVisibility(View.VISIBLE);
        String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_name)).getText().toString();
        String price = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_price)).getText().toString();
        String piece = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_piece)).getText().toString();
        String bar_code = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_bar_code)).getText().toString();

        String line_place = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_place)).getText().toString();
        String[] parts = line_place.split("/");
        String line = parts[0];
        String place = parts[1];

        getFirebaseResources(bar_code);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progress_bar.setVisibility(View.GONE);
        Intent intent = new Intent(this, DetailProductActivity.class);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();


        edit.putString("product_title", title);
        edit.putString("product_price", price);
        edit.putString("product_piece", piece);
        edit.putString("product_bar_code", bar_code);
        edit.putString("product_line", line);
        edit.putString("product_place", place);

        edit.commit();

        startActivity(intent);
    }

    private void getFirebaseResources(String b_code) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Product").child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String bar_code = dataSnapshot.child("bar-code").getValue().toString();
                    if(bar_code.equals(b_code)) {
                    //    int id = Integer.valueOf(dataSnapshot.child("id").getValue().toString());
                        String imageRes = dataSnapshot.child("image").getValue().toString();
                        String desc = dataSnapshot.child("description").getValue().toString();
                        Log.i("getFirebase", "" + imageRes + " " + desc);

                        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        myEdit.putString("fr_image", imageRes);
                        myEdit.putString("fr_desc", desc);
                        myEdit.commit();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


