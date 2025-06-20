package com.example.dws.Controllers;


import com.example.dws.DTOs.CommentDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.CommentRepository;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import com.example.dws.Repositories.UserRepository;
import com.example.dws.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import com.example.dws.Service.ProductService;
import com.example.dws.Service.ShopService;
import com.example.dws.Service.UserService;
import com.example.dws.Service.CommentService;

import java.util.Collection;
import java.util.Optional;


@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;

    @GetMapping("/comments/{shopId}/{commentId}")
    public String getCommentsById(@PathVariable("commentId") long commentId,@PathVariable("shopId") long shopId ,Model model) {
        Optional<CommentDTO> comment = commentService.findById(commentId);
        Optional<ShopDTO> shop = shopService.findById(shopId);
        if(shop.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        if (comment.isPresent()) {
            model.addAttribute("comment", comment.get());
            model.addAttribute("shop", shop.get());
            return "showComment";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El comentario seleccionado no existe");
        }
    }
    @PostMapping("/comments/{shopId}/{commentId}/deleteComment")
    public String deleteComment(
            @PathVariable long commentId,
            @PathVariable long shopId,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        Optional<CommentDTO> commentOpt = commentService.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El comentario seleccionado no existe");
        }

        CommentDTO comment = commentOpt.get();

        Optional<ShopDTO> shopDTO = shopService.findById(shopId);
        if (shopDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }


        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !comment.user().getUserName().equals(currentUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para borrar este comentario");
        }

        commentService.delete(commentId, shopId);
        return "redirect:/shops/" + shopId;
    }


}
