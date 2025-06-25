package com.example.dws.DTOs;

public record CommentBasicDTO(
        Long commentId,
        UserBasicDTO user,
        String issue,
        String message,
        long shopID
){
}