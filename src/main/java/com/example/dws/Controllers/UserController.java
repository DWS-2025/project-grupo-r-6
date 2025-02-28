package com.example.dws.Controllers;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/users/{userID}")
    public String getShopingCart(@PathVariable("userID") long userID, Model model){
        User user = userRepository.findByID(userID);
        if(user != null){
            model.addAttribute("user", user);
            return "showShoppingCart";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
    }

    @GetMapping("/users/name/")
    public String getShopingCartByName(@RequestParam("userName") String userName, Model model){
        User user = userRepository.findByName(userName);
        if(user != null){
            model.addAttribute("user", user);
            return "showShoppingCart";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
    }
}
