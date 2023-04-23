package com.example.ala.controller;

import com.example.ala.view.ReportActivity;
import com.example.ala.model.ReportModel;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ReportController {
    private ReportActivity view;
    private ReportModel model;

    public ReportController(ReportActivity view) {
        this.view = view;
        model = new ReportModel(this);
    }

    public void setWarehouseReport() {
        try {
            model.execWarehouseReport();
        }
        catch (NoSuchElementException e){
            setValueAfterWarehouseReport(0, 0+"", "-");
        }
    }

    public void setValueAfterWarehouseReport(long count_product, String sum_price, String mostRepeatedProduct) {
        view.txt_count_product.setText(count_product + " kus/ů");
        view.txt_sum_price.setText(sum_price + " Kč");
        view.txt_most_product.setText(mostRepeatedProduct);
    }

    public void setOrderReport(String date_from, String date_to) {
        model.execOrderReport(date_from, date_to);
    }

    public void setValueAfterOrderReport(int count_order, String sum_price_with_dph, String sum_price_without_dph) {
        view.txt_count_order.setText(count_order+"");
        view.txt_price_with_DPH.setText(sum_price_with_dph + " Kč");
        view.txt_price_without_DPH.setText(sum_price_without_dph + " Kč");
    }

    public void setGraphData(BarData barData, ArrayList<String> dates) {

       // view.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dates));
        view.barChart.setData(barData);

    }
}
