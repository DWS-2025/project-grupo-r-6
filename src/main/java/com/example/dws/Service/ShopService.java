package com.example.dws.Service;

import com.example.dws.DTOs.CommentDTO;
import com.example.dws.DTOs.GeneralMapper;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
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
    @Autowired
    private GeneralMapper generalMapper;

    public List<ShopDTO> findAll(){
        return generalMapper.ToListShopDTO(shopRepository.findAll());
    }

    public Optional<ShopDTO> findById(Long id){
        ShopDTO shopDTO= shopToShopDTO(shopRepository.findById(id));
        return Optional.of(shopDTO);
    }

    public void save(ShopDTO shopDTO){
        shopRepository.save(shopDTOToShop(shopDTO));
    }
    public void save(Shop shop){
        shopRepository.save(shop);
    }

    public void deleteById(Long id){
        shopRepository.deleteById(id);
    }

    @Transactional
    public void saveComment(ShopDTO shopDTO, CommentDTO commentDTO){
        Shop shop= shopDTOToShop(shopDTO);
        Comment comment= generalMapper.commentDTOToComment(commentDTO);
        shop.getComments().add(comment);
        commentRepository.save(comment);
        shopRepository.save(shop);
    }

    public List<Comment> getComments(ShopDTO shopDTO){
        Shop shop= shopDTOToShop(shopDTO);
        return shop.getComments();
    }
    public void saveProduct(ShopDTO shopDTO, ProductDTO productDTO){
        Shop shop= shopDTOToShop(shopDTO);
        Product product= generalMapper.productDTOToProduct(productDTO);
        shop.getProducts().add(product);
        shopRepository.save(shop);
    }
    public void removeProductFromAllShops(ProductDTO productDTO) {
        Product product= generalMapper.productDTOToProduct(productDTO);
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

    private ShopDTO shopToShopDTO(Optional<Shop> shop) {
        return generalMapper.shopToShopDTO(shop.get());
    }
    private Shop shopDTOToShop(ShopDTO shopDTO) {
        return generalMapper.shopDTOToShop(shopDTO);
    }
    private List<ShopDTO> shopToShopDTO(List<Shop> shop) {
        return generalMapper.ToListShopDTO(shop);
    }
}
