package com.example.dws.Repositories;

import com.example.dws.Entities.Product;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Repository

public class ProductRepository {

    private HashMap<AtomicLong, Product> products;
    AtomicLong id;

    public void save(Product product) {
        products.put(product.getProductId(), product);
    }
    public Product findById(AtomicLong productId) {
        return products.get(productId);
    }
    public void deleteById(AtomicLong productId) {
        products.remove(productId);
    }
    public Map<AtomicLong, Product> getAllProducts() {
        return products;
    }

}
