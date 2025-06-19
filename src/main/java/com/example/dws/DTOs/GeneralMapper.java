package com.example.dws.DTOs;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GeneralMapper {
    ShopDTO shopToShopDTO(Shop shop);
    ProductDTO productToProductDTO(Product product);
    @Mapping(source = "shop.shopID", target = "shopID")
    CommentDTO commentToCommentDTO(Comment comment);
    Shop shopDTOToShop(ShopDTO shopDTO);
    Product productDTOToProduct(ProductDTO productDTO);
    Comment commentDTOToComment(CommentDTO commentDTO);
    List<ShopDTO> ToListShopDTO(List<Shop> shops);
    List<ProductDTO> ToListProductDTO(List<Product> products);
    List<CommentDTO> ToListCommentDTO(List<Comment> comments);
    User userDTOToUser(UserDTO userDTO);
    List<UserDTO> ToListUserID(List<User> users);
    UserDTO userToUserDTO(User user);
}
