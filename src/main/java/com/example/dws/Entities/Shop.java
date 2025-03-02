package com.example.dws.Entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Shop {
    private String shopName;
    private static AtomicLong counter= new AtomicLong(0);// Use AtomicLong to manage the shop ID
    private long shopID;
    private HashMap<Long, Product> products;  // Use long as the key for products
    private HashMap<Long, Comment> comments;
    private String imageName;

    public Shop(String shopName, String imageName) {
        this.shopName = shopName;
        this.shopID = counter.getAndIncrement();  // Initialize AtomicLong with 0
        this.products = new HashMap<>();
        this.comments = new HashMap<>();
        this.imageName = imageName;
    }

    // Getter y Setter for shopName
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    // Getter y Setter for shopID
    public long getShopID() {
        return shopID;  //Return the value of shopID as a long
    }

    public void setShopID(long shopID) {
        this.shopID=shopID;
        // Set the value of shopID
    }

    // Getter y Setter for products
    public HashMap<Long, Product> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Long, Product> products) {
        this.products = products;
    }

    public Collection<Product> getAllProducts() {
        return this.products.values();
    }

    // Method to remove a product
    public void removeProduct(Product product) {
        this.products.remove(product.getProductId());  // Delete by product ID
    }

    public HashMap<Long, Comment> getComments() {
        return comments;
    }
    public void setComment(HashMap<Long, Comment> comments) {this.comments = comments;}
    public Collection<Comment> allComments() {
        return this.comments.values();
    }

    public void setComments(HashMap<Long, Comment> comments) {
        this.comments = comments;
    }
}

