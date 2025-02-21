package com.example.dws.Controllers;


import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.CommentRepository;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import com.example.dws.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;


@Controller
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/comments/{commentId}")
    public String getCommentsById(@PathVariable("commentId") long commentId, Model model) {
        Comment comment = commentRepository.findById(commentId);
        if (comment != null) {
            model.addAttribute("comment", comment);
            return "showComment"; // Vista que muestra los detalles de la tienda
        } else {
            return "error"; // Vista de error si no se encuentra la tienda
        }
    }

}
