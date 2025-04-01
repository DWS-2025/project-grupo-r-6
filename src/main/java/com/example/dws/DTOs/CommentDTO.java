package com.example.dws.DTOs;

import com.example.dws.Entities.User;

record CommentDTO(
        Long id,
        User user,
        String issue,
        String message
        ){
}
