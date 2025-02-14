package com.example.dws.Entities;

import com.example.dws.Enums.ShopType;

import java.util.*;

public class Shop {
    private String shopName;
    private int shopID;
    private HashMap<Integer, Product> products;

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
    public int getShopID() {
        return shopID;
    }
    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    // Getter y Setter para products
    public HashMap<Integer, Product> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Integer, Product> products) {
        this.products = products;
    }
    public void removeProduct(Product product){
        products.remove(product);
    }
}
