package com.example.ala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

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

    //TODO opravit hlavičku layoutu - přizpůsobit background
    //TODO při žádných vypsat "Prázdný sklad"

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

        String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_name)).getText().toString();

        Intent intent = new Intent(this, DetailProductActivity.class);
        intent.putExtra("product", title);
        startActivity(intent);
    }
}


