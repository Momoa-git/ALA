package com.vsb.ala.controller;

import android.view.View;

import com.vsb.ala.view.WarehouseActivity;
import com.vsb.ala.model.WarehouseModel;
import com.vsb.ala.model.object.Product;

import java.util.ArrayList;

public class WarehouseController {
    private WarehouseActivity view;
    private WarehouseModel model;

    public WarehouseController(WarehouseActivity view){
        this.view = view;
        model = new WarehouseModel(view, this);
    }

    public void setRecViewContent(ArrayList<Product> list, ArrayList<Product> list2)
    {
        model.setRecViewContent(list, list2);
    }

    public void getFirebaseResources(String bar_code){
        model.getFirebaseResources(bar_code);
    }

    public void onNotifyDataSetChanged() {
        this.view.productAdapter.notifyDataSetChanged();
    }

    public void setInvisibilityProgressBar() {
        this.view.progress_bar.setVisibility(View.GONE);
    }
}
