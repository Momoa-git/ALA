package com.vsb.ala.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vsb.ala.R;
import com.vsb.ala.controller.NewProductController;
import com.vsb.ala.service.InternetService;
import com.vsb.ala.view.dialog.InternetWarningDialog;

public class NewProductActivity extends AppCompatActivity implements InternetWarningDialog.InternetWarningDialogListener {

    private NewProductController controller;
    public static EditText edT_name_product, edT_price, edT_bar, edT_ks, edT_line, edT_place;
    private Button btn_scan;
    public static Button btn_add_item;
    private InternetService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        controller = new NewProductController(this);

        edT_name_product = findViewById(R.id.edT_name_product);
        edT_bar = findViewById(R.id.edT_bar);
        btn_scan = findViewById(R.id.btn_scan);
        edT_name_product = findViewById(R.id.edT_name_product);
        edT_price= findViewById(R.id.edT_price);
        btn_add_item = findViewById(R.id.btn_add_item);
        edT_ks = findViewById(R.id.edT_ks);
        edT_line = findViewById(R.id.edT_line);
        edT_place = findViewById(R.id.edT_place);

        service = new InternetService(this);



        btn_scan.setOnClickListener(v -> startActivity(new Intent(NewProductActivity.this, ScannerAddActivity.class)));



        btn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sh = getSharedPreferences("MyPref", MODE_PRIVATE);
                int id_list_product = sh.getInt("sp_id", 0);

                Boolean valid_input = checkValidValues();

                if (valid_input) {
                    controller.addNewProduct(edT_name_product.getText().toString(), edT_price.getText().toString(), edT_bar.getText().toString(),
                            Integer.valueOf(edT_ks.getText().toString()), edT_line.getText().toString(), edT_place.getText().toString(), id_list_product);
                }

                fillEmptyAll();
                if(!service.checkConnection()){
                    InternetWarningDialog dialog = new InternetWarningDialog();
                    dialog.show(getSupportFragmentManager(),"internet warning dialog");
                }

            }

            private void fillEmptyAll() {

                edT_name_product.setText("");
                edT_price.setText("");
                edT_line.setText("");
                edT_place.setText("");
                edT_bar.setText("");
                edT_ks.setVisibility(View.INVISIBLE);
                edT_line.setVisibility(View.INVISIBLE);
                edT_place.setVisibility(View.INVISIBLE);
                btn_add_item.setVisibility(View.INVISIBLE);


            }

            private Boolean checkValidValues() {

                if (edT_ks.getText().toString().matches(""))
                {
                    Toast.makeText(NewProductActivity.this, "Pole musí být vyplněné.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (Integer.parseInt(edT_ks.getText().toString()) == 0 || Integer.valueOf(edT_line.getText().toString()) == 0 || Integer.valueOf(edT_place.getText().toString()) == 0)
                {
                    Toast.makeText(NewProductActivity.this, "Hodnoty se nesmí rovnat 0.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                else{
                    return true;
                }

            }


        });



    }
    @Override
    public void applyAfterCheckConnection(){

    }




}


