package com.example.dws.DTOs;

import java.util.List;

public record UserBasicDTO (
        Long id,
        String userName,
        String email,
        List<ProductDTO> userProducts
){
}
