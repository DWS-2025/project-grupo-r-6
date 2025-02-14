package com.example.dws.Entities;


import com.example.dws.Enums.Size;
import com.example.dws.Enums.Type;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;


public class Product {
    private String productName;
    private AtomicLong productId;
    private double productPrize;
    private HashMap<AtomicLong, Product> shops;

    public Product(String productName, double productPrize) {
        this.productName = productName;
        this.productPrize = productPrize;
        this.shops = new HashMap<>();
    }

    public void deleteShop(Shop shop){
        this.shops.remove(shop);
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public AtomicLong getProductId() {
        return productId;
    }

    public void setProductId(AtomicLong productId) {
        this.productId = productId;
    }

    public double getProductPrize() {
        return productPrize;
    }

    public void setProductPrize(double productPrize) {
        this.productPrize = productPrize;
    }

    public HashMap<AtomicLong, Product> getShops() {
        return shops;
    }

    public void setShops(HashMap<AtomicLong, Product> shops) {
        this.shops = shops;
    }

}
