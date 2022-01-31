package com.example.ala;

public class Product {
    private int id;
    private int id_list_product;
    private String name;
    private String price;
    private String bar_code;
    private String line;
    private String place;

    public Product() {

    }

    public Product(int id, int id_list_product, String name, String price, String bar_code, String line, String place) {
        this.id = id;
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

    public int getId(){return id;}

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
