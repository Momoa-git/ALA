package com.vsb.ala.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vsb.ala.R;
import com.vsb.ala.adapter.ProductAdapter;
import com.vsb.ala.controller.WarehouseController;
import com.vsb.ala.model.object.Product;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class WarehouseActivity extends AppCompatActivity implements ProductAdapter.OnDetailListener {

    private WarehouseController controller;

    private RecyclerView recyclerView;
    public ProgressBar progress_bar;
    public ProductAdapter productAdapter;
    ArrayList<Product> list, list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        controller = new WarehouseController(this);

        recyclerView = findViewById(R.id.recycler_view);
        progress_bar = findViewById(R.id.progress_bar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        productAdapter = new ProductAdapter(this, list, list2, this);
        recyclerView.setAdapter(productAdapter);



        controller.setRecViewContent(list, list2);


    }

    @Override
    public void onDetailClick(int position) {
        progress_bar.setVisibility(View.VISIBLE);
        String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_name)).getText().toString();
        String price = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_price)).getText().toString();
        String piece = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_piece)).getText().toString();
        String bar_code = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_bar_code)).getText().toString();

        String line_place = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txt_place)).getText().toString();
        String[] parts = line_place.split("/");
        String line = parts[0];
        String place = parts[1];

        controller.getFirebaseResources(bar_code);

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

}


