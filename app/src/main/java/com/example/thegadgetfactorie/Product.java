package com.example.thegadgetfactorie;

public class Product {
    String title;
    String manufacturer;
    String category;
    String price;
    String stocklevel;
    String image;

    //empty constructor
    public Product(){

    }

    public Product(String title, String manufacturer, String category, String price, String stocklevel, String image){
        this.title = title;
        this.manufacturer = manufacturer;
        this.category = category;
        this.price = price;
        this.stocklevel = stocklevel;
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

    public String getStocklevel() {
        return stocklevel;
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

    public void setStocklevel(String stocklevel) {
        this.stocklevel = stocklevel;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
