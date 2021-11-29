package com.example.ala;

public class Product {
    private int id;
    private String name;
    private String price;
    private String bar_code;
    private String line;
    private String place;

    public Product() {

    }

    public Product(int id, String name, String price, String bar_code, String line, String place) {
        this.id = id;
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
