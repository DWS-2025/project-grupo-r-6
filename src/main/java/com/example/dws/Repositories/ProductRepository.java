package com.example.dws.Repositories;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {
    private HashMap<Long, Product> products = new HashMap<>();
    private AtomicLong nextId = new AtomicLong(1);

    public ProductRepository() {
        Product product1 = new Product("Product 1", 15);
        product1.setProductId(nextId.getAndIncrement());
        products.put(product1.getProductId(), product1);

        Product product2 = new Product("Product 2", 20);
        product2.setProductId(nextId.getAndIncrement());
        products.put(product2.getProductId(), product2);
    }

    public void save(Product product) {
        if (product.getProductId() == 0) {
            long id = nextId.getAndIncrement();
            product.setProductId(id);
        }
        this.products.put(product.getProductId(), product);
    }

    public Product findById(long productId) {
        return products.get(productId);
    }

    public void deleteById(long productId) {
        this.products.remove(productId);
    }

    public Collection<Product> findAll() {
        return products.values();
    }

    public void addShopToProduct(long productId, Shop shop) {
        Product product = products.get(productId);
        if (product != null) {
            product.getShops().put(shop.getShopID(), shop);
        }
    }

    public void removeShopFromProduct(long productId, Shop shop) {
        Product product = products.get(productId);
        if (product != null) {
            product.getShops().remove(shop.getShopID());
        }
    }
}
