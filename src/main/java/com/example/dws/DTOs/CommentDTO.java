package com.example.dws.DTOs;

import com.example.dws.Entities.User;

public record CommentDTO(
        Long id,
        User user,
        String issue,
        String message
        ){
}
