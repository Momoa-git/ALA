package com.example.ala.Inventory;

import java.io.Serializable;

public class Status implements Serializable {

    private String name;
    private int status_bar;

    public Status(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus_bar(int status_bar) {
        this.status_bar = status_bar;
    }

    public String getName() {
        return name;
    }

    public int getStatus_bar() {
        return status_bar;
    }
}
