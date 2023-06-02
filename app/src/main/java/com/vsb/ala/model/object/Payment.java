package com.vsb.ala.model.object;

import java.util.Observable;

public class Payment extends Observable {
    private String price, date_pay, type_pay;
    private int discount;
    private boolean paid;
    public Order order;

    Payment(Order order){
      this.order = order;
        PaymentDetailSender sender = new PaymentDetailSender(this);
        this.addObserver(sender);
    }

    public String getPrice() {
        return price;
    }

    public String getDate_pay() {
        return date_pay;
    }

    public String getType_pay() {
        return type_pay;
    }

    public int getDiscount() {
        return discount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate_pay(String date_pay) {
        this.date_pay = date_pay;
    }

    public void setType_pay(String type_pay) {
        this.type_pay = type_pay;
    }

    public void setDiscount(long discount) {
        this.discount = (int) discount;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
        setChanged();
        notifyObservers();
    }


}
