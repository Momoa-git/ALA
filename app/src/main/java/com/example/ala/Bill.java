package com.example.ala;

public class Bill {
    int register_number;
    String adress_office, date_order, date_pay, type_pay, customer_name, customer_email, costomer_phone;

    public Bill(int register_number, String adress_office, String date_order, String date_pay, String type_pay, String customer_name, String customer_email, String costomer_phone) {
        this.register_number = register_number;
        this.adress_office = adress_office;
        this.date_order = date_order;
        this.date_pay = date_pay;
        this.type_pay = type_pay;
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.costomer_phone = costomer_phone;
    }
}
