package com.example.dws.Controllers;

import com.example.dws.DTOs.ProductDTO;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.UserRepository;
import com.example.dws.Service.UserService;
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
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    // Show shopping cart of a user by its ID
    @GetMapping("/users/{userID}")
    public String getShopingCart(@PathVariable("userID") long userID, Model model){
        Optional<User> user = userRepository.findById(userID);
        if(user.isPresent()){
            model.addAttribute("user", user.get());
            List<Product> allProducts = user.get().allProducts();
            model.addAttribute("allProducts", allProducts);
            return "showShoppingCart";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
    }
    // Show shopping cart of a user by its name
    @GetMapping("/users/getLogged/")
    public String getShopingCartByLoggedUser(Model model){
        User user = userService.getLoggedUser();
            model.addAttribute("user", user);
            List<Product> allProducts = user.allProducts();
            model.addAttribute("allProducts", allProducts);
            return "showShoppingCart";
    }
}
