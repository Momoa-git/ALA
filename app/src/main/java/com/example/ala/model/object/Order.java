package com.example.ala.model.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {

    private String key;
    private int  id_product, id_order, id_customer, discount, order_number;
    private String date_order, price, date, time, status, office, id_list_product, date_pay, time_pay, type_pay, customer_name, customer_email, customer_phone,adress_office;
    private boolean paid;
    public Items items = Items.getInstance();
    List<Long> piecesofProduct = new ArrayList<>();
    List<String> namesofProduct = new ArrayList<>();
    List<Integer> registerNumsofProduct = new ArrayList<>();
    List<Double> pricesOfProduct = new ArrayList<>();

    public Order() {

    }

    public Order(int order_number, int id_product, int id_order, String id_list_product, int id_customer, String date, String time, String status, String office) {
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
    public void addPiecesofProduct(long adding_value){
        piecesofProduct.add(adding_value);
    }

    public void addNamesofProduct(String adding_value){
        namesofProduct.add(adding_value);
    }

    public void addRegisterNumsofProduct(int adding_value){
        registerNumsofProduct.add(adding_value);
    }

    public void addPricesOfProduct(double adding_value)
    {
        pricesOfProduct.add(adding_value);
    }

    public int getRegisterNumber(int position)
    {
        return registerNumsofProduct.get(position);
    }

    public int getSizeRegisterNums()
    {
        return registerNumsofProduct.size();
    }

    public void removeAllPieces()
    {
        piecesofProduct.clear();
        namesofProduct.clear();
        registerNumsofProduct.clear();
        pricesOfProduct.clear();
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public void setAdress_office(String adress_office) {
        this.adress_office = adress_office;
    }

    public String getDate_order() {
        return date_order;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public String getAdress_office() {
        return adress_office;
    }

    public void setDiscount(long discount) {
        this.discount = (int) discount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
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

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public int getDiscount() {
        return discount;
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

    public boolean isPaid() {
        return paid;
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

    public void setId_list_product(String id_list_product) {
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

    public String getId_list_product() {
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
