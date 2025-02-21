package com.example.dws.Controllers;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
        return "user_not_found";
    }

    @GetMapping("/users/name/")
    public String getShopingCartByName(@RequestParam("userName") String userName, Model model){
        User user = userRepository.findByName(userName);
        if(user != null){
            model.addAttribute("user", user);
            return "showShoppingCart";
        }
        return "user_not_found";
    }
}
