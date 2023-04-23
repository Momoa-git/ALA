package com.example.ala.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ala.R;
import com.example.ala.controller.ReportController;
import com.github.mikephil.charting.charts.BarChart;

import java.util.Calendar;

public class ReportActivity extends AppCompatActivity {

    private ReportController controller;
    public TextView txt_count_product, txt_most_product, txt_sum_price, txt_date_from, txt_date_to, txt_count_order, txt_price_with_DPH, txt_price_without_DPH;
    private Button btn_warehouse, btn_do, btn_od, btn_search_order;
    private ProgressBar progress_bar;
   public BarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        controller = new ReportController(this);


        txt_count_product = findViewById(R.id.txt_count_product);
        txt_most_product = findViewById(R.id.txt_most_product);
        txt_sum_price = findViewById(R.id.txt_sum_price);
        progress_bar = findViewById(R.id.progress_bar);
        btn_warehouse = findViewById(R.id.btn_warehouse);
        btn_od = findViewById(R.id.btn_od);
        btn_do = findViewById(R.id.btn_do);
        txt_date_to = findViewById(R.id.txt_date_to);
        txt_date_from = findViewById(R.id.txt_date_from);
        btn_search_order = findViewById(R.id.btn_search_order);
        txt_count_order = findViewById(R.id.txt_count_order);
        txt_price_with_DPH = findViewById(R.id.txt_price_with_DPH);
        txt_price_without_DPH = findViewById(R.id.txt_price_without_DPH);
        barChart = findViewById(R.id.chart);


        progress_bar.setVisibility(View.INVISIBLE);


        controller.setWarehouseReport();


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        btn_warehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportActivity.this, WarehouseActivity.class));
            }
        });


        DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar updateDate = Calendar.getInstance();
                updateDate.set(year, month, dayOfMonth);
                month = month + 1;
                txt_date_from.setText(dayOfMonth + "." + month + ". " + year);
            }
        }, year, month, day);


        DatePickerDialog datePickerDialog2 = new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar updateDate = Calendar.getInstance();
                updateDate.set(year, month, dayOfMonth);
                month = month + 1;
                txt_date_to.setText(dayOfMonth + "." + month + ". " + year);
            }
        }, year, month, day);

        btn_od.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        btn_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog2.show();
            }
        });

        btn_search_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_date_from.getText().toString().matches("") || txt_date_to.getText().toString().matches(""))
                    Toast.makeText(getApplicationContext(), "Nutné vyplnit interval v kalendáři.", Toast.LENGTH_SHORT).show();
                else
                    controller.setOrderReport(txt_date_from.getText().toString(), txt_date_to.getText().toString());
            }
        });


    }




    }



