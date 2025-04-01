package com.example.dws.DTOs;

import com.example.dws.Entities.Product;

import java.util.List;

record UserDTO(
        Long id,
        String name,
        String email,
        List<Product> products
) {
}
