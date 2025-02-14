package com.example.dws.Repositories;

import com.example.dws.Entities.Product;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Repository

public class ProductRepository {

    private Map<Long, Product> productsMap = new ConcurrentHashMap();
    private AtomicLong lastID = new AtomicLong();
    private int productCounter = 0;


    public ProductRepository() {
    }

    public ProductRepository(Map<Long, Product> productsMap) {
        this.productsMap = productsMap;
    }

    public Product putProductsMap(Product product) {
        long id = this.lastID.incrementAndGet();
        product.setId(id);
        this.productsMap.put(id, product);
        ++this.productCounter;
        return product;
    }



    public Product addMyproduct(Product product, Long id){
        if (id != null) {
            this.productsMap.put(id, product);
        }
        return product;
    }
    public Collection<Product> returnMyProducts(){
        return this.productsMap.values();
    }


}
