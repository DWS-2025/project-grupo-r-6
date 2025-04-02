package com.example.dws.DTOs;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;

import java.sql.Blob;
import java.util.List;

public record ShopDTO(
        Long shopID,
        String shopName,
        List<Product> products,
        List<Comment> comments,
        String imageName,
        Blob imageFile
) {

}
