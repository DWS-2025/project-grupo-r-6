package com.example.dws.Entities;



import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;


public class Product {
    private String productName;
    private static AtomicLong counter = new AtomicLong(0);
    private long productId;
    private double productPrize;
    private HashMap<Long, Shop> shops;

    public Product(String productName, double productPrize) {
        this.productName = productName;
        this.productPrize = productPrize;
        this.shops = new HashMap<>();
        this.productId = counter.getAndIncrement();
    }

    public void deleteShop(Shop shop){
        this.shops.remove(shop.getShopID());
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

    public HashMap<Long, Shop> getShops() {
        return shops;
    }

    public Collection<Shop> allShops(){
        return this.shops.values();
    }

    public void setShops(HashMap<Long, Shop> shops) {
        this.shops = shops;
    }
}