package com.example.dws.Entities;

import com.example.dws.Enums.ShopType;

import java.util.*;

public class Shop {
    private String shopName;
    private long shopID;
    private HashMap<Long, Product> products;

    public Shop(String shopName) {
        this.shopName = shopName;
        this.products = new HashMap<>();
    }
    // Getter y Setter para shopName
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    // Getter y Setter para shopID
    public long getShopID() {
        return shopID;
    }
    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    // Getter y Setter para products
    public HashMap<Long, Product> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Long, Product> products) {
        this.products = products;
    }
    public void removeProduct(Product product){
        products.remove(product);
    }
}
