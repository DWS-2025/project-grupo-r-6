package com.example.dws.Service;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.CommentRepository;
import com.example.dws.Repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void saveComment(Shop shop, Comment comment){
        shop.getComments().add(comment);
        commentRepository.save(comment);
        shopRepository.save(shop);
    }
    public void removeProductFromAllShops(Product product) {
        for (Shop shop : shopRepository.findAll()) {
            shop.getProducts().remove(product);
        }
    }
}
