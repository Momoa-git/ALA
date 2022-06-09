package com.example.ala;

public class Product {
    private int register_number;
    private int id_list_product;
    private String name;
    private String price;
    private String bar_code;
    private String line;
    private String place;

    public Product() {

    }

    public Product(int register_number, int id_list_product, String name, String price, String bar_code, String line, String place) {
        this.register_number = register_number;
        this.id_list_product = id_list_product;
        this.name = name;
        this.price = price;
        this.bar_code = bar_code;
        this.line = line;
        this.place = place;
    }

    public Product(String name, String price, String bar_code) {
        this.name = name;
        this.price = price;
        this.bar_code = bar_code;

    }

    public void setRegister_number(int register_number) {
        this.register_number = register_number;
    }

    public void setId_list_product(int id_list_product) {
        this.id_list_product = id_list_product;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getRegister_number(){return register_number;}

    public int getId_list_product(){return id_list_product;}

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getBar_code() {
        return bar_code;
    }

    public String getLine() {
        return line;
    }

    public String getPlace() {
        return place;
    }
}
