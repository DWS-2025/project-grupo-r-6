package com.example.dws.DTOs;

import com.example.dws.Entities.Shop;

import java.util.List;

public record ProductDTO(
        Long id,
        String productName,
        Double productPrize,
        List<Shop> shops
) {
}
