package com.vsb.ala.controller;

import android.widget.Toast;

import com.vsb.ala.view.NewProductActivity;
import com.vsb.ala.model.NewProductModel;

public class NewProductController {
    private NewProductModel model;
    private NewProductActivity view;

    public NewProductController(NewProductActivity view){
        this.view = view;
        model = new NewProductModel(this);
    }

    public void addNewProduct(String name, String price, String bar_code, Integer piece, String line, String place, int id_list_product) {
        model.addNewProduct(name, price, bar_code, piece, line, place, id_list_product);
    }

    public void displayToast(int piece){
        Toast.makeText(view, piece + " Product(s) added!" , Toast.LENGTH_LONG).show();
    }

}
