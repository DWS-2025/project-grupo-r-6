package com.example.dws.Service;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }
    public void saveShopInProduct(Product product, Shop shop) {
        if (!product.getShops().contains(shop)) {
            product.getShops().add(shop);
        }
        productRepository.save(product);
    }
    public void deleteById(long id){
        productRepository.deleteById(id);
    }
    public void save(Product product) {
        productRepository.save(product);
    }
    public void update(Product product, String name, double price) {
        product.setProductName(name);
        product.setProductPrize(price);
        productRepository.save(product);
    }
    public void removeShopFromAllProducts(Shop shop) {
        for (Product product : productRepository.findAll()) {
            product.getShops().remove(shop);
        }
    }
}
