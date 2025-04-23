package com.example.dws.DTOs;

import com.example.dws.Entities.User;

public record CommentDTO(
        Long commentId,
        User user,
        String issue,
        String message
        ){
}
