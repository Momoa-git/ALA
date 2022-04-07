package com.example.ala;

import java.util.Date;

public class Order {

    int order_number, id_product, id_order, id_list_product, id_customer;
    String date, time, status, office;

    public Order() {

    }

    public Order(int order_number, int id_product, int id_order, int id_list_product, int id_customer, String date, String time, String status, String office) {
        this.order_number = order_number;
        this.id_product = id_product;
        this.id_order = id_order;
        this.id_list_product = id_list_product;
        this.id_customer = id_customer;
        this.date = date;
        this.time = time;
        this.status = status;
        this.office = office;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public void setId_list_product(int id_list_product) {
        this.id_list_product = id_list_product;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public int getOrder_number() {
        return order_number;
    }

    public int getId_product() {
        return id_product;
    }

    public int getId_order() {
        return id_order;
    }

    public int getId_list_product() {
        return id_list_product;
    }

    public int getId_customer() {
        return id_customer;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getOffice() {
        return office;
    }
}
