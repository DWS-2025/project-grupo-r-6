package com.example.dws.RestControllers;

import com.example.dws.DTOs.CommentDTO;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.Service.CommentService;
import com.example.dws.Service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ShopService shopService;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllProducts() {
        List<CommentDTO> commentDTOs = commentService.findAll();
        return ResponseEntity.ok(commentDTOs);
    }

    // GET a specific comment by shop‐ID and comment‐ID
    @GetMapping("/{shopID}/{commentID}")
    public ResponseEntity<CommentDTO> getCommentById(
            @PathVariable long shopID,
            @PathVariable long commentID
    ) {
        Optional<ShopDTO> shopDTO = shopService.findById(shopID);
        if (shopDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        Optional<CommentDTO> commentDTO = commentService.findById(commentID);
        if (commentDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El comentario seleccionado no existe");
        }
        return ResponseEntity.ok(commentDTO.get());
    }

    // DELETE a specific comment by shop‐ID and comment‐ID
    @DeleteMapping("/{shopID}/{commentID}")
    public ResponseEntity<String> deleteComment(
            @PathVariable long shopID,
            @PathVariable long commentID
    ) {
        Optional<ShopDTO> shopDTO = shopService.findById(shopID);
        if (shopDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        Optional<CommentDTO> commentDTO = commentService.findById(commentID);
        if (commentDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El comentario seleccionado no existe");
        }
        commentService.delete(commentID, shopID);
        return ResponseEntity.ok("Comentario eliminado correctamente");
    }
}

