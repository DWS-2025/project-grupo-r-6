package com.example.dws.Entities;



import jakarta.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Entity
public class User {
    private static AtomicLong counter= new AtomicLong(0);
    private long id;
    private String name;
    private String email;
    private HashMap<Long, Product> userProducts;


    public User(String name, String email){
        this.name = name;
        this.email = email;
        this.userProducts = new HashMap<>();
        this.id = counter.getAndIncrement();
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

