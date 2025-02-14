package com.example.dws.Repositories;

import com.example.dws.Entities.Product;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Repository

public class ProductRepository {

    private Map<Long, Product> productsMap = new ConcurrentHashMap<>();
    private AtomicLong lastID = new AtomicLong();

    // Agregar un nuevo producto con ID autogenerado
    public Product addProduct(Product product) {
        long id = lastID.incrementAndGet();
        product.setId(id);
        productsMap.put(id, product);
        return product;
    }

    // Agregar un producto con ID específico
    public Product addProductWithId(Product product, Long id) {
        if (id != null) {
            product.setId(id);
            productsMap.put(id, product);
        }
        return product;
    }

    // Obtener todos los productos
    public Collection<Product> getAllProducts() {
        return productsMap.values();
    }


}
