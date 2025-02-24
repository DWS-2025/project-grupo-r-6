package com.example.dws.Entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Shop {
    private String shopName;
    private static AtomicLong counter= new AtomicLong(0);// Usar AtomicLong para manejar el ID de la tienda
    private long shopID;
    private HashMap<Long, Product> products;  // Usar long como clave para productos
    private HashMap<Long, Comment> comments;

    public Shop(String shopName) {
        this.shopName = shopName;
        this.shopID = counter.getAndIncrement();  // Inicializar AtomicLong con 0
        this.products = new HashMap<>();
        this.comments = new HashMap<>();
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
        return shopID;  // Devolver el valor de shopID como long
    }

    public void setShopID(long shopID) {
        this.shopID=shopID;
        // Establecer el valor de shopID
    }

    // Getter y Setter para products
    public HashMap<Long, Product> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Long, Product> products) {
        this.products = products;
    }

    public Collection<Product> getAllProducts() {
        return this.products.values();
    }

    // Método para eliminar un producto
    public void removeProduct(Product product) {
        this.products.remove(product.getProductId());  // Eliminar por el ID del producto
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

