package com.example.dws.DTOs;

import com.example.dws.Entities.Product;

import java.util.List;

public record UserDTO(
        Long id,
        String userName,
        String password,
        String email,
        List<String> roles,
        List<ProductDTO> userProducts
) {
}
