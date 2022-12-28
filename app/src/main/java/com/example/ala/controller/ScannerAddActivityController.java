package com.example.ala.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.example.ala.NewProductActivity;
import com.example.ala.view.ScannerAddActivity;
import com.example.ala.model.ScannerAddActivityModel;

public class  ScannerAddActivityController {
    private ScannerAddActivityModel model;
    private ScannerAddActivity view;

    private Context context;
   public SharedPreferences preferences;

    public ScannerAddActivityController(ScannerAddActivity view, Context contextAct){
        this.view = view;
        model = new ScannerAddActivityModel(this);
        context = contextAct;
        preferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }

    public void getProductByCode(String bar_code)
    {
        model.getProductByCode(bar_code);
    }

    public void onNewProductDetail(String name, String price, String code) {
        NewProductActivity.edT_name_product.setText(name);
        NewProductActivity.edT_price.setText(price + ".00 Kč");
        NewProductActivity.edT_bar.setText(code);
    }

    public void setVisibility() {
        NewProductActivity.edT_ks.setVisibility(View.VISIBLE);
        NewProductActivity.edT_line.setVisibility(View.VISIBLE);
        NewProductActivity.edT_place.setVisibility(View.VISIBLE);
        NewProductActivity.btn_add_item.setVisibility(View.VISIBLE);
    }



}
