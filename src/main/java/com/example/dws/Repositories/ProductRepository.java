package com.example.dws.Repositories;

import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByproductName(String productName);

    @Query("SELECT p FROM Product p WHERE p.productPrize BETWEEN :from AND :to")
    List<Product> findByPrecioBetween(@Param("from") Integer from, @Param("to") Integer to);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%')) AND p.productPrize BETWEEN :from AND :to")
    List<Product> findByNameContainingIgnoreCaseAndPrecioBetween(@Param("name") String name, @Param("from") Integer from, @Param("to") Integer to);

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
