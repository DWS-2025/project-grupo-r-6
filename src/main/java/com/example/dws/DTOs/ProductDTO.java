package com.example.dws.DTOs;

import com.example.dws.Entities.Shop;

import java.util.List;

record ProductDTO(
        Long id,
        String productName,
        double productPrize,
        List<Shop> shops
) {
}
