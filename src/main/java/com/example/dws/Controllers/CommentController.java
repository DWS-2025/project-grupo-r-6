package com.example.dws.Controllers;


import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.CommentRepository;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import com.example.dws.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;


@Controller
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;

    @GetMapping("/comments/{shopId}/{commentId}")
    public String getCommentsById(@PathVariable("commentId") long commentId,@PathVariable("shopId") long shopId ,Model model) {
        Comment comment = commentRepository.findById(commentId);
        Shop shop = shopRepository.findById(shopId);
        if(shop == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        if (comment != null) {
            model.addAttribute("comment", comment);
            model.addAttribute("shop", shop);
            return "showComment"; // Vista que muestra los detalles de la tienda
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El comentario seleccionado no existe");
        }
    }
    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam long commentId, @RequestParam long shopId) {
        if(commentRepository.findById(commentId) != null){
            // Buscar la tienda a la que pertenece el comentario
            Shop shop = shopRepository.findById(shopId);
            if(shop == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
            }

            // Eliminar el comentario del HashMap de la tienda
            shop.getComments().remove(commentId);
            shopRepository.save(shop); // Guardar la tienda actualizada

            // Finalmente, eliminar el comentario del repositorio
            commentRepository.deleteById(commentId);

            return "redirect:/shops/" + shopId; // Redirige a la tienda afectada
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El comentario seleccionado no existe");
        }

    }

}
