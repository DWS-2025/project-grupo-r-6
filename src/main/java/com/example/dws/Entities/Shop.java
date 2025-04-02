package com.example.dws.Entities;

import jakarta.persistence.*;

import java.sql.Blob;
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
    @OneToMany (mappedBy = "shop")
    private List<Comment> comments;
    private String imageName;
    @Lob
    private Blob imageFile;

    public Shop(String shopName, String imageName) {
        this.shopName = shopName;
        this.products = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.imageName = imageName;

    }

    public Shop() {

    }


    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
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
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public List<Comment> allComments() {
        return this.comments;
    }


    public Blob getImageFile() {
        return imageFile;
    }
}

