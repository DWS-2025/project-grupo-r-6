package com.example.dws.RestControllers;

import com.example.dws.DTOs.GeneralMapper;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.UserBasicDTO;
import com.example.dws.DTOs.UserDTO;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.User;
import com.example.dws.Service.UserService;
import com.example.dws.Service.ProductService;
import com.example.dws.Service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopService shopService;
    @Autowired
    private GeneralMapper generalMapper;

    // GET all users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        boolean isAdmin = userService.isAdmin();
        if (isAdmin) {
            return ResponseEntity.ok(userService.findAll());
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no tiene permisos");
        }
    }

    // GET a user by ID
    @GetMapping("/{userID}")
    public ResponseEntity<UserBasicDTO> getUserById(@PathVariable long userID) {
        boolean isAdmin = userService.isAdmin();
        long actual = userService.getLoggedUser().getId();
        Optional<UserBasicDTO> userDTO = userService.findUserById(userID);
        if (userDTO.isPresent()) {
            if(isAdmin || actual == userID){
                return ResponseEntity.ok(userDTO.get());
            }else{
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no tiene permisos");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
        }
    }

    // GET logged‐in user's shopping cart
    @GetMapping("/me/cart")
    public ResponseEntity<List<ProductDTO>> getLoggedUserCart() {
        User logged = userService.getLoggedUser();
        List<ProductDTO> cartDTOs = new ArrayList<>();

        for (Product product : logged.allProducts()) {
            Optional<ProductDTO> optionalProductDTO = productService.findById(product.getProductId());
            if (optionalProductDTO.isPresent()) {
                cartDTOs.add(optionalProductDTO.get());
            }
        }
        return ResponseEntity.ok(cartDTOs);
    }

    @PutMapping("/{userID}")
    public ResponseEntity<String> updateUser(@PathVariable long userID,@RequestBody  UserDTO userDTO) {
        boolean isAdmin = userService.isAdmin();
        long actual = userService.getLoggedUser().getId();
        Optional<UserDTO> existingUser = userService.findById(userID);
        if(isAdmin || actual == userID){
            if (existingUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
            }

            if (userDTO.userName() == null || userDTO.userName().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del usuario no puede estar vacío");
            }
            if (userDTO.email() == null || userDTO.email().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo email no puede estar vacío");
            }
            if (userDTO.password() != null && !userDTO.password().isBlank()) {
                if (!userDTO.password().equals(userDTO.confirmPassword())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas deben coincidir");
                }
                userService.updatePassword(userID, userDTO.password());
            }

            userService.update(userID, userDTO);
            return ResponseEntity.ok("Usuario actualizado correctamente");
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no tiene permisos");
        }
    }

    @DeleteMapping("/users/{userID}")
    public ResponseEntity<String> deleteUser(@PathVariable long userID) {
        boolean isAdmin = userService.isAdmin();
        long actual = userService.getLoggedUser().getId();
        if (userService.findById(userID).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        if (isAdmin || actual == userID){
            userService.deleteById(userID);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no tiene permisos");
        }
    }

/*
    // POST create new user
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        if (userDTO.name() == null || userDTO.name().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del usuario no puede estar vacío");
        }
        if (userDTO.email() == null || userDTO.email().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email del usuario no puede estar vacío");
        }
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado correctamente");
    }

    // DELETE a user
    @DeleteMapping("/{userID}")
    public ResponseEntity<String> deleteUser(@PathVariable long userID) {
        Optional<UserDTO> userDTO = userService.findById(userID);
        if (userDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
        }
        // Aquí podrías eliminar productos del carrito o cualquier limpieza adicional
        userService.findById(userID).ifPresent(dto -> {
            // Si hay lógica extra, agrégala antes de borrar
        });
        // Suponiendo que el repositorio de usuarios se maneja en UserService.save(...) y no hay delete explícito:
        // Si necesitas eliminar al usuario, asegúrate de implementar userService.deleteById(userID) en UserService.
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "El borrado de usuario debe implementarse en UserService");
    }

    // POST add a product to the logged‐in user's cart
    @PostMapping("/me/addProduct")
    public ResponseEntity<String> addProductToCart(@RequestBody ProductDTO productDTO) {
        if (productDTO.productName() == null || productDTO.productName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío");
        }
        if (productDTO.productId() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del producto no puede ser 0");
        }
        Optional<ProductDTO> existing = productService.findById(productDTO.productId());
        if (existing.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
        userService.addProduct(existing.get());
        return ResponseEntity.ok("Producto añadido al carrito del usuario logueado");
    }

    // DELETE remove a product from the logged‐in user's cart
    @DeleteMapping("/me/removeProduct/{productID}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable long productID) {
        Optional<ProductDTO> existing = productService.findById(productID);
        if (existing.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
        userService.removeProductFromUser(existing.get());
        return ResponseEntity.ok("Producto eliminado del carrito del usuario logueado");
    }

 */
}

