package com.example.ala.model.object;

import java.util.ArrayList;
import java.util.List;

public class  Inventory {
    private static Inventory singleton_inventory = null;
    List<ProductInOrder> items = new ArrayList<>();

    private Inventory()
    {

    }


    public void addItem(ProductInOrder product)
    {
        items.add(product);
    }

    public void removeItems()
    {
        items.clear();
    }

    public ProductInOrder getItem(int index)
    {
        return items.get(index);
    }

    public int getSize()
    {
       return items.size();
    }

    public static Inventory getInstance()
    {
        if(singleton_inventory == null)
            singleton_inventory = new Inventory();

        return singleton_inventory;
    }

}
