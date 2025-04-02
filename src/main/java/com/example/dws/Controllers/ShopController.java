package com.example.dws.Controllers;

import com.example.dws.DTOs.CommentDTO;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.DTOs.UserDTO;
import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.CommentRepository;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import com.example.dws.Repositories.UserRepository;
import com.example.dws.Service.CommentService;
import com.example.dws.Service.ShopService;
import com.example.dws.Service.UserService;
import com.example.dws.Service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class ShopController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    private static final Path IMAGES_FOLDER = Paths.get("uploads");

    // Show all shops
    @GetMapping
    public String getAllShops(Model model) {
        List<ShopDTO> shopsDTO = shopService.findAll();
        model.addAttribute("shops", shopsDTO);
        return "index"; // View showing all the shops
    }

    // View details of a shop by its ID
    @GetMapping("/shops/{shopID}")
    public String getShopById(@PathVariable("shopID") long shopId, Model model) {
        Optional<ShopDTO> shopDTO = shopService.findById(shopId);
        if (shopDTO.isPresent()) {
            model.addAttribute("shop", shopDTO.get());
            return "showShop"; // View showing shop details
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
    }


    // Save new shop
    @PostMapping("/save")
    public String saveShop(@RequestParam String shopName,
                           @RequestParam String imageName,
                           @RequestParam MultipartFile image) throws IOException {
        if (shopName != null && !shopName.trim().isEmpty()) {
            if(!image.isEmpty() && imageName.trim().isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Si quieres añadir una imagen el nobre de la imagen no puede ser vacío");
            } else {
                /* Cambiar foto a BBDD

                // Create a directory if it doesn´t exist
                Files.createDirectories(IMAGES_FOLDER);

                //Save image
                Path imagePath = IMAGES_FOLDER.resolve(imageName + ".jpg");
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                 */

                // Create and save the shop in the repository
                Shop shop = new Shop(shopName, imageName);
                shopService.save(shop, image);
            }
            return "redirect:/";
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la tienda no puede ser vacío");
        }

    }

    @GetMapping("/shopImage/{shopID}")
    public void viewImage(@PathVariable Long shopID, HttpServletResponse response) throws SQLException, IOException {
        Blob imageBlob = shopService.getShopImage(shopID);

        if (imageBlob != null) {
            response.setContentType("image/jpeg"); // Cambia según el formato de la imagen
            response.getOutputStream().write(imageBlob.getBytes(1, (int) imageBlob.length()));
            response.getOutputStream().flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Delete the shop of the products that have it assigned and the shop is deleted
    @PostMapping("/delete")
    public String deleteShop(@RequestParam long shopID) {
        if(shopService.findById(shopID).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        } else{
            productService.removeShopFromAllProducts(shopService.findById(shopID).get());
            shopService.deleteById(shopID); // Remove the shop by its ID
            return "redirect:/"; // Redirect to shop list
        }
    }
    // Add new product to the shop
    @PostMapping("/shops/{shopID}/products/new")
    public String newProductToShop(ProductDTO productDTO, @PathVariable long shopID) {
        if(shopService.findById(shopID).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        if (productDTO.productName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio del producto no puede estar vacío");
        }
        if (productDTO.productName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío");
        }
        productService.saveShopInProduct(productDTO, shopService.findById(shopID).get());
        /*    PASAR ESTO AL PRODUCT SERVICE
        Shop shop = shopRepository.findById(shopID);
        shop.getProducts().put(product.getProductId(), product);
        product.getShops().put(shopID, shop);
        productRepository.save(product);
        shopRepository.save(shop);
         */
        return "redirect:/shops/" + shopID;
    }
    // Add new comment to the shop
    @PostMapping("/shops/{shopID}/comments/new")
    public String newCommentToShop(CommentDTO commentDTO, @PathVariable long shopID) {
        if (!commentDTO.user().getName().isEmpty()){
            Optional<ShopDTO> shopDTO = shopService.findById(shopID);
            if (shopDTO.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
            }
            Optional<UserDTO> useraux = userService.findByName(commentDTO.user().getName());
            if (useraux.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
            }
            shopService.saveComment(shopDTO.get(), commentDTO);
            return "redirect:/shops/" + shopID;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del usuario no puede estar vacio");
    }
}

