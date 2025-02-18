package com.example.dws.Entities;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Shop {
    private String shopName;
    private AtomicLong shopID;  // Usar AtomicLong para manejar el ID de la tienda
    private HashMap<Long, Product> products;  // Usar long como clave para productos

    public Shop(String shopName) {
        this.shopName = shopName;
        this.shopID = new AtomicLong(0);  // Inicializar AtomicLong con 0
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
        return shopID.get();  // Devolver el valor de shopID como long
    }

    public void setShopID(long shopID) {
        this.shopID.set(shopID);
        // Establecer el valor de shopID
    }

    // Getter y Setter para products
    public HashMap<Long, Product> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Long, Product> products) {
        this.products = products;
    }

    // Método para eliminar un producto
    public void removeProduct(Product product) {
        this.products.remove(product.getProductId());  // Eliminar por el ID del producto
    }
}

