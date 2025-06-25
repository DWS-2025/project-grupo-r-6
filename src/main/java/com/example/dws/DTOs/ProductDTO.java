package com.example.dws.DTOs;

import com.example.dws.Entities.Shop;

import java.util.ArrayList;
import java.util.List;

public record ProductDTO(
        Long productId,
        String productName,
        Double productPrize,
        List<ShopBasicDTO> shops,
        String originalFileName,
        String storedFileName
) {}


