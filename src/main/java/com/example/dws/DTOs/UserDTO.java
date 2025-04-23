package com.example.dws.DTOs;

import com.example.dws.Entities.Product;

import java.util.List;

public record UserDTO(
        Long id,
        String name,
        String email,
        List<ProductDTO> userProducts
) {
}
