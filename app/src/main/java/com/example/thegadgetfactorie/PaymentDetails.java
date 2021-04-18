package com.example.thegadgetfactorie;

public class PaymentDetails {
    String cardHolder, cardNumber, expirayDate, cvv;

    public PaymentDetails(String cardHolder, String cardNumber, String expirayDate, String cvv) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expirayDate = expirayDate;
        this.cvv = cvv;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirayDate() {
        return expirayDate;
    }

    public void setExpirayDate(String expirayDate) {
        this.expirayDate = expirayDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
