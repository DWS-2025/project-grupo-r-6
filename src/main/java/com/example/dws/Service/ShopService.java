package com.example.dws.Service;

import com.example.dws.DTOs.*;
import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.CommentRepository;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import jakarta.transaction.Transactional;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private SanitizationService sanitizationService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private GeneralMapper generalMapper;
    @Autowired
    private UserService userService;

    public List<ShopDTO> findAll(){
        System.out.println(generalMapper.ToListShopDTO(shopRepository.findAll()));
        return generalMapper.ToListShopDTO(shopRepository.findAll());
    }
    public List<ShopExDTO> findAllShops(){
        System.out.println(generalMapper.ToListShopExDTO(shopRepository.findAll()));
        return generalMapper.ToListShopExDTO(shopRepository.findAll());
    }

    public Optional<ShopDTO> findById(Long id) {
        Optional<Shop> optionalShop = shopRepository.findById(id);
        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();
            ShopDTO shopDTO = generalMapper.shopToShopDTO(shop);
            return Optional.of(shopDTO);
        } else {
            return Optional.empty();
        }
    }
    public Optional<ShopExDTO> findShopById(Long id) {
        Optional<Shop> optionalShop = shopRepository.findById(id);
        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();
            ShopExDTO shopDTO = generalMapper.shopToShopExDTO(shop);
            return Optional.of(shopDTO);
        } else {
            return Optional.empty();
        }
    }


    public Optional<ShopDTO> findByName(String name){
        Shop shop = shopRepository.findByshopName(name).get();
        return Optional.of(generalMapper.shopToShopDTO(shop));
    }


    public void save(ShopDTO shopDTO){
        Shop shop = shopDTOToShop(sanitizationService.sanitizeShopDTO(shopDTO));
        shopRepository.save(shop);
    }

    public void save(Shop shop){
        shopRepository.save(shop);
    }


    public void deleteById(Long id){
        shopRepository.deleteById(id);
    }

    public void saveComment(ShopDTO shopDTO, CommentDTO commentDTO) {
        Optional<Shop> optionalShop = shopRepository.findById(shopDTO.shopID());
        if (optionalShop.isEmpty()) {
            throw new RuntimeException("La tienda no existe");
        }
        Shop shop = optionalShop.get();

        User loggedUser = userService.getLoggedUser();
        Comment comment = sanitizationService.sanitizeAndMapToComment(commentDTO, shop, loggedUser);
        comment.setShop(shop);
        comment.setUser(userService.getLoggedUser());
        commentRepository.save(comment);

        shop.getComments().add(comment);
        shopRepository.save(shop);
    }


    public List<Comment> getComments(ShopDTO shopDTO){
        Shop shop= shopDTOToShop(shopDTO);
        return shop.getComments();
    }

    public Page<CommentDTO> paginateShopComments(ShopDTO shopDTO, Pageable pageable) {
        List<CommentDTO> allComments = shopDTO.comments(); // Lista completa

        int total = allComments.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);

        List<CommentDTO> paginatedList = allComments.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, total);
    }

    public void removeProductFromAllShops(ProductDTO productDTO) {
        Product product= generalMapper.productDTOToProduct(productDTO);
        for (Shop shop : shopRepository.findAll()) {
            shop.getProducts().remove(product);
        }
    }

    public void save(ShopDTO shopDTO, MultipartFile imageFile) throws IOException {
        Shop shop = shopDTOToShop(sanitizationService.sanitizeShopDTO(shopDTO));
        if(!imageFile.isEmpty()) {
            shop.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(),
                    imageFile.getSize()));
        } this.save(shop);
    }

    public void saveShopWithImage(Shop shop, String imagePath) throws IOException {
        ClassPathResource resource = new ClassPathResource("static/" + imagePath);
        InputStream inputStream = resource.getInputStream();

        MockMultipartFile multipartFile = new MockMultipartFile(
                "imageFile",
                imagePath,
                "image/jpeg",
                inputStream
        );


        this.save(generalMapper.shopToShopDTO(shop), multipartFile);
    }

    public void removeAllProductsFromShop(long shopID){
        Optional<Shop> shop = shopRepository.findById(shopID);
        shop.get().getProducts().clear();
        shopRepository.save(shop.get());
    }

    @Transactional
    public void removeAllCommentsFromShop(long shopID) {
        commentRepository.deleteByShop_ShopID(shopID);
    }



    public Blob getShopImage(Long shopID) {
        return shopRepository.findById(shopID)
                .map(Shop::getImageFile)
                .orElse(null);
    }

    public ShopDTO shopToShopDTO(Optional<Shop> shop) {
        return generalMapper.shopToShopDTO(shop.get());
    }
    private Shop shopDTOToShop(ShopDTO shopDTO) {
        return generalMapper.shopDTOToShop(shopDTO);
    }
    private List<ShopDTO> shopToShopDTO(List<Shop> shop) {
        return generalMapper.ToListShopDTO(shop);
    }
}
