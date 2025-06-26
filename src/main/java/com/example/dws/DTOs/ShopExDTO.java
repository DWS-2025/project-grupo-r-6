package com.example.dws.DTOs;

import java.util.List;

public record ShopExDTO(Long shopID,
                        String shopName,
                        List<ProductBasicDTO> products,
                        List<CommentBasicDTO> comments,
                        String imageName,
                        String imageUrl) {
}
