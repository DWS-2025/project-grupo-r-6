package com.example.dws.Entities;

import com.example.dws.Enums.Size;
import com.example.dws.Enums.Type;

import java.util.Collection;
import java.util.HashMap;


public class User {
    private long id;
    private String name;
    private String email;
    private HashMap<Long, Product> userProducts;

    public User() {
        this.userProducts = new HashMap<>();
    }
    public User(String name, String email){
        this.name = name;
        this.email = email;
        this.userProducts = new HashMap<>();
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public Collection<Product> allProducts(){
        return this.userProducts.values();
    }

    public void addProduct(Long productId, Product product) {
        this.userProducts.put(productId, product);
    }

    public void removeProduct(Long productId) {
        this.userProducts.remove(productId);
    }
}

