package com.example.dws.Service;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.CommentRepository;
import com.example.dws.Repositories.ShopRepository;
import jakarta.transaction.Transactional;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CommentRepository commentRepository;

    public List<Shop> findAll(){
        return shopRepository.findAll();
    }

    public Optional<Shop> findById(Long id){
        return shopRepository.findById(id);
    }

    public void save(Shop shop){
        shopRepository.save(shop);
    }

    public void deleteById(Long id){
        shopRepository.deleteById(id);
    }

    @Transactional
    public void saveComment(Shop shop, Comment comment){
        shop.getComments().add(comment);
        commentRepository.save(comment);
        shopRepository.save(shop);
    }
    public void saveProduct(Shop shop, Product product){
        shop.getProducts().add(product);
        shopRepository.save(shop);
    }
    public void removeProductFromAllShops(Product product) {
        for (Shop shop : shopRepository.findAll()) {
            shop.getProducts().remove(product);
        }
    }

    public void save(Shop shop, MultipartFile imageFile) throws IOException {
        if(!imageFile.isEmpty()) {
            shop.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(),
                    imageFile.getSize()));
        } this.save(shop);
    }

    public Blob getShopImage(Long shopID) {
        return shopRepository.findById(shopID)
                .map(Shop::getImageFile)
                .orElse(null);
    }
}
