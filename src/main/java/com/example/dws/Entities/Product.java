package com.example.dws.Entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long productId;

    private String productName;

    private double productPrize;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Shop> shops;

    public Product(String productName, double productPrize) {
        this.productName = productName;
        this.productPrize = productPrize;
        this.shops = new ArrayList<>();
    }

    public Product() {
        this.shops = new ArrayList<>();
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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getProductPrize() {
        return productPrize;
    }

    public void setProductPrize(double productPrize) {
        this.productPrize = productPrize;
    }

    public List<Shop> getShops() {
        return this.shops;
    }

    public List<Shop> allShops(){
        return this.shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}