package com.example.dws.DTOs;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;

import java.sql.Blob;
import java.util.List;

public record ShopDTO(
        Long shopID,
        String shopName,
        List<ProductBasicDTO> products,
        List<CommentDTO> comments,
        String imageName,
        Blob imageFile
) {

}
