package com.example.dws.Entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false)
    private String userName; // Necesario para login

    private String name;
    private String email;

    @Column(nullable = false)
    private String password; // Necesario para login

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>(); // Roles: ["USER"], ["ADMIN"], etc.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> userProducts = new ArrayList<>();

    public User() {}

    public User(String userName, String name, String email, String password, List<String> roles) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public List<String> getRoles() { return roles; }

    public void setRoles(List<String> roles) { this.roles = roles; }

    public List<Product> getUserProducts() { return userProducts; }

    public void addProduct(Product product) {
        this.userProducts.add(product);
        product.setUser(this); // Asegura que el producto sepa quién es su dueño
    }

    public void removeProduct(Product product) {
        this.userProducts.remove(product);
        product.setUser(null);
    }
}
