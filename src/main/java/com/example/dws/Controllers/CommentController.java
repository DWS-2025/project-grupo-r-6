package com.example.dws.Controllers;


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
        Optional<Comment> comment = commentService.findById(commentId);
        Optional<Shop> shop = shopService.findById(shopId);
        if(shop.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        if (comment.isPresent()) {
            model.addAttribute("comment", comment);
            model.addAttribute("shop", shop);
            return "showComment"; // View showing shop details
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El comentario seleccionado no existe");
        }
    }
    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam long commentId, @RequestParam long shopId) {
        if(commentService.findById(commentId).isPresent()){
            // Search the shop the comment belong to
            Optional<Shop> shop = shopService.findById(shopId);
            if(shop.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
            }

            // Remove comment from shop hashmap
            shop.get().getComments().remove(commentService.findById(commentId).get());
            shopService.save(shop.get()); // Save the updated shop

            // Finally, remove the comment from the repository
            commentService.deleteById(commentId);

            return "redirect:/shops/" + shopId; // Redirect to the affected shop
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El comentario seleccionado no existe");
        }

    }

}
