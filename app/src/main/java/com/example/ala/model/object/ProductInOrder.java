package com.example.ala.model.object;

public class ProductInOrder {
    private int id_product;
    private String name;
    private long piece;
    private double price;
    private int registration_num;


    public ProductInOrder() {

    }

    public ProductInOrder(String name, long piece, double price, int registration_num) {
        this.name = name;
        this.piece = piece;
        this.price = price;
        this.registration_num = registration_num;
    }

    public int getId_product() {
        return id_product;
    }

    public String getName() {
        return name;
    }

    public long getPiece() {
        return piece;
    }

    public double getPrice() {
        return price;
    }

    public int getRegistration_num() {
        return registration_num;
    }
}
