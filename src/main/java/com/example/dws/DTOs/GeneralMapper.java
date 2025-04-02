package com.example.dws.DTOs;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GeneralMapper {
    ShopDTO shopToShopDTO(Shop shop);
    ProductDTO productToProductDTO(Product product);
    CommentDTO commentToCommentDTO(Comment comment);
    Shop shopDTOToShop(ShopDTO shopDTO);
    Product productDTOToProduct(ProductDTO productDTO);
    Comment commentDTOToComment(CommentDTO commentDTO);
    List<ShopDTO> ToListShopDTO(List<Shop> shops);
}
