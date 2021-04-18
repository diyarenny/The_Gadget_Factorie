package com.example.thegadgetfactorie;

public class Product {
    String title;
    String manufacturer;
    String category;
    String price;
    String stockLevel;
    String image;

    //empty constructor
    public Product(){

    }

    public Product(String title, String manufacturer, String category, String price, String stockLevel, String image){
        this.title = title;
        this.manufacturer = manufacturer;
        this.category = category;
        this.price = price;
        this.stockLevel = stockLevel;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getStockLevel() {
        return stockLevel;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStockLevel(String stockLevel) {
        this.stockLevel = stockLevel;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
