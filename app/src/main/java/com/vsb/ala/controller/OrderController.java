package com.vsb.ala.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.vsb.ala.model.object.Order;
import com.vsb.ala.R;
import com.vsb.ala.model.OrderModel;
import com.vsb.ala.view.OrderActivity;

import java.util.ArrayList;

public class OrderController {
  private OrderModel model;
  private OrderActivity view;
  int id;
  float result_price;
    String old_sale;
    long sale_F;


   public OrderController(OrderActivity view){
       this.view = view;
       model = new OrderModel(this);
   }

    public void setRecViewContent()
    {
       model.setRecViewContent();
    }

    public void onNotifyDataSetChanged() {
       this.view.adapter.notifyDataSetChanged();
    }

    public void onSetItems(ArrayList<Order> order)
    {
      //  this.view.adapter.setItems(order);
    }

    public int getOrderID(int position)
    {
        return model.getOrderID(position);
    }

    public void getOrderFirebaseResources(int id_order)
    {
        id = id_order;
        model.getOrderFirebRes(id_order);
    }

    public void setOrderResources(String order_number, String date_order, String time_order, String status, String names, String reg_numbers, String type_pay, String paid, String price, String discount, String date_pay) {
       this.view.numberOrder.setText("Objednávka " + order_number);
       this.view.txt_name_product.setText(names);
       this.view.txt_register_num.setText(reg_numbers);
       this.view.txt_price.setText(price + " Kč s DPH");
       this.view.txt_paid.setText(paid);
       this.view.txt_type_payment.setText(type_pay);
       getPaid(paid, date_pay);
       setStatus(status);
       this.view.txt_date_order.setText(date_order + " " + time_order);
       this.view.txt_discount.setText(discount);
       this.view.txt_date_pay.setText(date_pay);

    }

    private void getPaid(String paid, String date_pay) {
       if(paid.equals("ANO")) {
           this.view.btn_edit_sale.setVisibility(View.GONE);
           this.view.btn_payment.setText("PŘEDÁNÍ");
           this.view.txt_date_pay.setText(date_pay);
           this.view.txt_date_pay.setVisibility(View.VISIBLE);
           this.view.title_date_pay.setVisibility(View.VISIBLE);
       }
       else {
           this.view.btn_edit_sale.setVisibility(View.VISIBLE);
           this.view.btn_payment.setText("PLATBA");
           this.view.txt_date_pay.setVisibility(View.GONE);
           this.view.title_date_pay.setVisibility(View.GONE);
       }
    }

    private void setStatus(String status)
    {
        switch (status) {
            case "PE":
                this.view.txt_status.setText("Čekající");
                this.view.img_status_bar.setImageResource(R.drawable.status_bar_pe);
                this.view.title_locate.setVisibility(View.GONE);
                this.view.txt_locate.setVisibility(View.GONE);
                this.view.txt_date_locate.setVisibility(View.GONE);
                this.view.title_registr_num.setVisibility(View.GONE);
                this.view.txt_register_num.setVisibility(View.GONE);
                //btn
                this.view.btn_payment.setVisibility(View.GONE);
                this.view.btn_storno.setVisibility(View.GONE);
                this.view.txt_description.setVisibility(View.VISIBLE);
                this.view.txt_description.setText("ČEKÁNÍ NA NASKLADNĚNÍ");
                break;
            case "IP":
                this.view.txt_status.setText("Vyřizuje se");
                this.view.img_status_bar.setImageResource(R.drawable.status_bar_ip);
                this.view.txt_description.setVisibility(View.GONE);
                //btn
                this.view.btn_payment.setVisibility(View.VISIBLE);
                this.view.btn_storno.setVisibility(View.VISIBLE);
                break;
            case "CO":
                this.view. txt_status.setText("Vyřízena");
                this.view. img_status_bar.setImageResource(R.drawable.status_bar_co);
                this.view.txt_description.setVisibility(View.VISIBLE);
                this.view.txt_description.setText("OBJEDNÁVKA BYLA VYŘÍZENA");
                //btn
                this.view.btn_edit_sale.setVisibility(View.GONE);
                this.view.btn_payment.setVisibility(View.GONE);
                this.view.btn_storno.setVisibility(View.GONE);
                break;
            case "CA":
                this.view. txt_status.setText("Stornována");
                this.view.img_status_bar.setImageResource(R.drawable.status_bar_ca);
                this.view.txt_description.setVisibility(View.VISIBLE);
                //btn
                this.view.btn_edit_sale.setVisibility(View.GONE);
                this.view.btn_payment.setVisibility(View.GONE);
                this.view.btn_storno.setVisibility(View.GONE);
                break;

        }
    }

    public void setCustomerResources(String fname, String lname, String email, String phone) {
       this.view.txt_name_customer.setText(fname + " " + lname);
       this.view.txt_email_customer.setText(email);
       this.view.txt_phone_customer.setText(phone);
    }

    public void setOfficeResources(String name, String address) {
       this.view.txt_offic_address.setText(address);
       this.view.txt_office_name.setText(name);
    }


    public void setInvisibleDatePay() {
       this.view.txt_date_pay.setVisibility(View.GONE);
       this.view.title_date_pay.setVisibility(View.GONE);
    }


    public void setAfterSale(String sale) {
        old_sale = view.txt_discount.getText().toString().replace("%", "");
        float old_sale_f;
        try {
             old_sale_f = Float.valueOf(old_sale);
        } catch (NumberFormatException e) {
            old_sale_f = 0;
        }

        view.txt_discount.setText(sale + " %");




        String newStr = this.view.txt_price.getText().toString().replace(",",".").replaceAll("[^0-9.]" ,"");
        float price = Float.valueOf(newStr);
        sale_F = Long.valueOf(sale);
        Log.i("vysledek", sale_F +" * "+ price);
        result_price = model.calculatePriceAfterSale(sale_F, price, old_sale_f);
        view.txt_price.setText(model.setPriceFormat(String.valueOf(result_price)) + " Kč s DPH");

    }

    public void setAfterStorno()
    {
        model.saveStornoStatus(id);
        view.txt_description.setText("OBJEDNÁVKA BYLA STORNOVÁNA");
    }


    public void setAfterPayment() {
           String priceStr = this.view.txt_price.getText().toString().replace(",",".").replaceAll("[^0-9.]" ,"");
           String saleStr = this.view.txt_discount.getText().toString().replace(",",".").replaceAll("[^0-9.]" ,"");
           if(!saleStr.equals(""))
               model.updateSaleAfterPay(Float.valueOf(priceStr),Integer.valueOf(saleStr),id);
           if(view.txt_paid.getText().toString().equals("NE"))
               model.updatePaymentAfterPay(id);

           model.updateStatusAfterPay(id);
           Log.i("payment", "Price: " + priceStr + " Sale: " + saleStr + " Payment: " + view.txt_paid.getText().toString());

    }

    public void setPDF(Context context)
    {

        model.loadPDF(context);
    }

    public void removeProducts()
    {
        model.removeProductFromOffice();
    }


}
