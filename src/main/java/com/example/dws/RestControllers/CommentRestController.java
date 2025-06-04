package com.example.dws.RestControllers;

import com.example.dws.DTOs.CommentDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.Service.CommentService;
import com.example.dws.Service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ShopService shopService;

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

    // POST create a new comment under a given shop
    @PostMapping("/{shopID}")
    public ResponseEntity<String> addCommentToShop(
            @PathVariable long shopID,
            @RequestBody CommentDTO commentDTO
    ) {
        Optional<ShopDTO> shopDTO = shopService.findById(shopID);
        if (shopDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        // Delegamos a ShopService para guardar el comentario ligado a la tienda
        shopService.saveComment(shopDTO.get(), commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Comentario añadido correctamente");
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

