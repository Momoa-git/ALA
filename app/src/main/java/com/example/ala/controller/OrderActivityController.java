package com.example.ala.controller;

import android.view.View;

import com.example.ala.Order;
import com.example.ala.R;
import com.example.ala.model.OrderActivityModel;
import com.example.ala.view.OrderActivityView;

public class OrderActivityController {
  private OrderActivityModel model;
  private OrderActivityView view;


   public OrderActivityController(OrderActivityView view){
       this.view = view;
       model = new OrderActivityModel(this);
   }

    public void setRecViewContent()
    {
       model.setRecViewContent();
    }

    public void onAddOrderToList(Order order){
       this.view.list.add(order);
    }

    public void onNotifyDataSetChanged() {
       this.view.adapter.notifyDataSetChanged();
    }

    public int getOrderID(int position)
    {
        return model.getOrderID(position);
    }

    public int onOrderID(int position) {
       return this.view.list.get(position).getId_order();
    }

    public void getOrderFirebaseResources(int id_order)
    {
        model.getOrderFirebaseResources(id_order);
    }

    public void setOrderResources(String order_number, String date_order, String time_order, String status, String office, String id_list_product, String type_pay, String paid, String price, String discount, String date_pay) {
       this.view.numberOrder.setText("Objednávka " + order_number);
       this.view.txt_price.setText(price + " Kč s DPH");
       this.view.txt_paid.setText(paid);
       this.view.txt_type_payment.setText(type_pay);
       setStatus(status);
       this.view.txt_date_order.setText(date_order + " " + time_order);
       this.view.txt_discount.setText(discount);
       this.view.txt_date_pay.setText(date_pay);

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
                break;
            case "IP":
                this.view.txt_status.setText("Vyřizuje se");
                this.view.img_status_bar.setImageResource(R.drawable.status_bar_ip);
                break;
            case "CO":
                this.view. txt_status.setText("Vyřízena");
                this.view. img_status_bar.setImageResource(R.drawable.status_bar_co);
                break;
            case "CA":
                this.view. txt_status.setText("Stornována");
                this.view.img_status_bar.setImageResource(R.drawable.status_bar_ca);
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

    public void setProductListResources(String nameStream) {
       this.view.txt_name_product.setText(nameStream);
    }

    public void setInvisibleDatePay() {
       this.view.txt_date_pay.setVisibility(View.GONE);
       this.view.title_date_pay.setVisibility(View.GONE);
    }
}
