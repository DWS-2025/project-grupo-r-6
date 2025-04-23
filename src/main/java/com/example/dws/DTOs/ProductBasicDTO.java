package com.example.dws.DTOs;

import java.util.List;

public record ProductBasicDTO(
        Long productId,
        String productName,
        Double productPrize
) {

}

