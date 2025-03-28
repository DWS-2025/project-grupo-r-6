package com.example.dws.Entities;

import jakarta.persistence.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long shopID;
    private String shopName;
    @ManyToMany (mappedBy = "shops")
    private List<Product> products;
    @OneToMany
    private List<Comment> comments;
    private String imageName;

    public Shop(String shopName, String imageName) {
        this.shopName = shopName;
        this.products = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.imageName = imageName;
    }

    public Shop() {

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
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getAllProducts() {
        return this.products;
    }

    // Method to remove a product
    public void removeProduct(Product product) {
        this.products.remove(product);  // Delete by product ID
    }

    public List<Comment> getComments() {
        return comments;
    }
    public void setComment(List<Comment> comments) {this.comments = comments;}
    public List<Comment> allComments() {
        return this.comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

