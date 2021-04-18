package com.example.thegadgetfactorie;

public class ShippingDetails {

    String addressLine1, addressLine2, country, eircode;

    public ShippingDetails(String addressLine1, String addressLine2, String country, String eircode) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.country = country;
        this.eircode = eircode;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEircode(String eircode) {
        this.eircode = eircode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCountry() {
        return country;
    }

    public String getEircode() {
        return eircode;
    }
}
