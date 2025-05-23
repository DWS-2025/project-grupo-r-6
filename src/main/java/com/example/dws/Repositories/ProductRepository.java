package com.example.dws.Repositories;

import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByproductName(String productName);

    /*
    private HashMap<Long, Product> products = new HashMap<>();
    private AtomicLong nextId = new AtomicLong(0);



    public void save(Product product) {
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

    public void removeShopFromAllProducts(long shopID){
        for (Product product : products.values()){
            product.getShops().remove(shopID);
        }
    }
     */
}
