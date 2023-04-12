package com.example.ala.model.object;

public class Payment {
    private String price, date_pay, time_pay, type_pay;
    private int discount;
    private boolean paid;

    Payment(){

    }

    public String getPrice() {
        return price;
    }

    public String getDate_pay() {
        return date_pay;
    }

    public String getTime_pay() {
        return time_pay;
    }

    public String getType_pay() {
        return type_pay;
    }

    public int getDiscount() {
        return discount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate_pay(String date_pay) {
        this.date_pay = date_pay;
    }

    public void setTime_pay(String time_pay) {
        this.time_pay = time_pay;
    }

    public void setType_pay(String type_pay) {
        this.type_pay = type_pay;
    }

    public void setDiscount(long discount) {
        this.discount = (int) discount;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
