package com.example.dws.Service;

import com.example.dws.DTOs.CommentDTO;
import com.example.dws.DTOs.GeneralMapper;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private GeneralMapper generalMapper;
    @Autowired
    private ShopRepository shopRepository;


    public Optional<ProductDTO> findById(long id) {
        return Optional.of(productToProductDTO(productRepository.findById(id)));
    }

    public Optional<ProductDTO> findByName(String name) {
        return Optional.of(productToProductDTO(productRepository.findByproductName(name)));
    }


    public void saveProductInShop(ProductDTO productDTO, ShopDTO shopDTO) {
        Product product = productDTOToProduct(productDTO);
        if(product.getShops() == null){
            product.setShops(new ArrayList<>());
        }
        Shop shop = generalMapper.shopDTOToShop(shopDTO);
        if (!product.getShops().contains(shop)) {
            product.getShops().add(shop);
        }
        if (!shop.getProducts().contains(product)) {
            shop.getProducts().add(product);
        }
        productRepository.save(product);

    }
    public void deleteById(long id){
        productRepository.deleteById(id);
    }
    public void save(ProductDTO productDTO) {
        Product product = productDTOToProduct(productDTO);
        productRepository.save(product);
    }
    public void update(Long id, ProductDTO productDTO) {
        Optional<Product> oldProduct = productRepository.findById(id);
        Product newProduct = productDTOToProduct(productDTO);
        oldProduct.get().setProductName(newProduct.getProductName());
        oldProduct.get().setProductPrize(newProduct.getProductPrize());
        productRepository.save(oldProduct.get());
    }
    public void removeShopFromAllProducts(ShopDTO shopDTO) {
        Optional<Shop> optionalShop = shopRepository.findById(shopDTO.shopID());
        if (optionalShop.isPresent()) {
            Shop shop = optionalShop.get();
            for (Product product : productRepository.findAll()) {
                if (product.getShops().contains(shop)) {
                    product.getShops().remove(shop);
                    productRepository.save(product);
                }
            }
        }
    }


    private ProductDTO  productToProductDTO(Optional<Product> product) {
        return generalMapper.productToProductDTO(product.get());
    }
    private Product productDTOToProduct(ProductDTO productDTO) {
        return generalMapper.productDTOToProduct(productDTO);
    }
}
