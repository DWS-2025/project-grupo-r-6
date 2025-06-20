package com.example.dws.Controllers;

import com.example.dws.DTOs.GeneralMapper;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.UserDTO;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.UserRepository;
import com.example.dws.Service.CommentService;
import com.example.dws.Service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private GeneralMapper generalMapper;

    // Show shopping cart of a user by its ID
    @GetMapping("/users/{userID}")
    public String getShopingCart(
            @PathVariable("userID") long userID,
            Model model,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        Optional<User> user = userRepository.findById(userID);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
        }

        User foundUser = user.get();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !foundUser.getUserName().equals(currentUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para ver este carrito");
        }

        model.addAttribute("user", foundUser);
        List<Product> allProducts = foundUser.allProducts();
        model.addAttribute("allProducts", allProducts);
        return "showShoppingCart";
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


    @GetMapping("/profile")
    public String me(Model model) {
        model.addAttribute("user", userService.getLoggedUserDTO());
        return "profile";
    }

    @GetMapping("/users/{userID}/edit")
    public String editUser(@PathVariable("userID") long userID, Model model) {
        Optional<UserDTO> user = userService.findById(userID);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        model.addAttribute("user", user.get());
        return "editUser";
    }
    @PostMapping("/users/{userID}/update/")
    public String updateUser(UserDTO userDTO, @PathVariable("userID") long userID,Model model ){
        Optional<UserDTO> user = userService.findById(userID);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
        }
        if (userDTO.email()== null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email del usuario no puede estar vacío");
        }
        if (userDTO.userName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del usuario no puede estar vacío");
        }
        if (userDTO.password() != null && !userDTO.password().isBlank()) {
            if (!userDTO.password().equals(userDTO.confirmPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
            }

            userService.updatePassword(userID, userDTO.password());
        }
        this.userService.update(userID,userDTO);
        model.addAttribute("user", user.get());
        return "updated_user";
    }

    @PostMapping("/users/{userID}/delete")
    public String deleteUser(@PathVariable long userID) {
        Optional<UserDTO> user = userService.findById(userID);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        commentService.deleteByUserId(userID);
        userService.deleteById(userID);
        return "deleted_user";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserDTO> users = userService.findAll();
        model.addAttribute("users", users);
        return "user_list";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String userName,
            @RequestParam String email,
            @RequestParam String password
    ) {
        // Verificar si el usuario ya existe
        if (userService.findByName(userName).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya está en uso");
        }

        //Create DTO with role USER
        User user = new User(userName, email, passwordEncoder.encode(password), "USER");
        UserDTO userDTO = generalMapper.userToUserDTO(user);
        userService.save(userDTO);

        return "redirect:/login"; // redirect to login
    }

}
