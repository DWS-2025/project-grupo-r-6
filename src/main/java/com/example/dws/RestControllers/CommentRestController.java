package com.example.dws.RestControllers;

import com.example.dws.DTOs.*;
import com.example.dws.Entities.Comment;
import com.example.dws.Service.CommentService;
import com.example.dws.Service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private GeneralMapper generalMapper;

    @GetMapping
    public ResponseEntity<List<CommentBasicDTO>> getAllComments() {
        List<CommentBasicDTO> commentDTOs = commentService.findAllComments();
        return ResponseEntity.ok(commentDTOs);
    }
    @GetMapping("/paginated")
    public ResponseEntity<Page<CommentDTO>> getPaginatedComments(
            @RequestParam Long shopID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentService.findByShopIdPaginated(shopID, pageable);

        Page<CommentDTO> commentDTOPage = commentPage.map(generalMapper::commentToCommentDTO);
        return ResponseEntity.ok(commentDTOPage);
    }



    // GET a specific comment by shop‐ID and comment‐ID
    @GetMapping("/{shopID}/{commentID}")
    public ResponseEntity<CommentBasicDTO> getCommentById(
            @PathVariable long shopID,
            @PathVariable long commentID
    ) {
        Optional<ShopDTO> shopDTO = shopService.findById(shopID);
        if (shopDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        Optional<CommentBasicDTO> commentDTO = commentService.findCommentById(commentID);
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

