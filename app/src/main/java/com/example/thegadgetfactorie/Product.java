package com.example.thegadgetfactorie;

import com.google.android.material.snackbar.Snackbar;

public class Product {
    String title;
    String manufacturer;
    String category;
    int price;
    String stocklevel;
    String image;

    //empty constructor
    public Product(){

    }

    public Product(String title, String manufacturer, String category, int price, String stocklevel, String image){
        this.title = title;
        this.manufacturer = manufacturer;
        this.category = category;
        this.price = price;
        this.stocklevel = stocklevel;
        this.image = image;
    }
}
