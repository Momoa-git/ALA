package com.vsb.ala.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.vsb.ala.view.NewProductActivity;
import com.vsb.ala.view.ScannerAddActivity;
import com.vsb.ala.model.ScannerAddModel;

public class ScannerAddController {
    private ScannerAddModel model;
    private ScannerAddActivity view;

    private Context context;
   public SharedPreferences preferences;

    public ScannerAddController(ScannerAddActivity view, Context contextAct){
        this.view = view;
        model = new ScannerAddModel(this);
        context = contextAct;
        preferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }

    public void getProductByCode(String bar_code)
    {
        model.getProductByCode(bar_code);
    }

    public void onNewProductDetail(String name, String price, String code) {
        NewProductActivity.edT_name_product.setText(name);
        NewProductActivity.edT_price.setText(price ); //+ ".00 Kč"
        NewProductActivity.edT_bar.setText(code);
    }

    public void setVisibility() {
        NewProductActivity.edT_ks.setVisibility(View.VISIBLE);
        NewProductActivity.edT_line.setVisibility(View.VISIBLE);
        NewProductActivity.edT_place.setVisibility(View.VISIBLE);
        NewProductActivity.btn_add_item.setVisibility(View.VISIBLE);
    }



}
