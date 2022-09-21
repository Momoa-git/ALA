package com.example.ala;

public class ProductInOrder {
    private int id_product;
    private String name;
    private int piece;
    private int price;
    private int registration_num;


    public ProductInOrder() {

    }

    public ProductInOrder(int id_product, String name, int piece, int price, int registration_num) {
        this.id_product = id_product;
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

    public int getPiece() {
        return piece;
    }

    public int getPrice() {
        return price;
    }

    public int getRegistration_num() {
        return registration_num;
    }
}
