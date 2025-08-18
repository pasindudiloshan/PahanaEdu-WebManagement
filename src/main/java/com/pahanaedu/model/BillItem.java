package com.pahanaedu.model;

import java.io.Serializable;

public class BillItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int billId;
    private String productId;  
    private int quantity;
    private double unitPrice;
    private double discountAmount; 
    private double finalPrice;

    // New field to hold numeric product ID (from products.id)
    private int productNumericId;

    public BillItem() {}

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }
    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }
    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getProductNumericId() {
        return productNumericId;
    }
    public void setProductNumericId(int productNumericId) {
        this.productNumericId = productNumericId;
    }
}

