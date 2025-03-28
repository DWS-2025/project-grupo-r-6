package com.example.dws.Controllers;

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
    /*
    @PostConstruct
    public void init() {

        Shop shop1 = new Shop("PullAndCow", "Pull");
        Shop shop2 = new Shop("Bresh", "Bresh");
        Shop shop3 = new Shop("XtraVarios", "XtraVarios");

        shopRepository.save(shop1);
        shopRepository.save(shop2);
        shopRepository.save(shop3);

        Product product1 = new Product("Camiseta", 20);
        Product product2 = new Product("Sudadera", 35);
        Product product3 = new Product("Zapatillas", 62.99);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        User user1 = new User("Adri", "adri@gmail.com");
        userRepository.save(user1);
        Comment comment1 = new Comment(user1, "Atención Al Cliente", "Tuve una excelente experiencia con el servicio al cliente. Fueron muy amables y me ayudaron a encontrar lo que buscaba. ¡Muy satisfecho!");
        Comment comment2 = new Comment(user1, " Calidad excelente", " Me encanta la calidad de la ropa que compré. Las prendas son cómodas, bien hechas y duran mucho. ¡Definitivamente volveré a comprar!");
        Comment comment3 = new Comment(user1, "Muy cómoda", "Compré una camiseta y unos pantalones, y ambos son increíblemente cómodos. La tela es suave y perfecta para el verano. Muy feliz con mi compra.");
        Comment comment4 = new Comment(user1, "Tienda muy bonita", "La tienda física tiene un ambiente muy agradable y moderno. Los productos están bien organizados y la experiencia de compra es muy cómoda.");
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);

        shop1.getComments().put(comment1.getCommentId(), comment1);
        shop1.getComments().put(comment2.getCommentId(), comment2);
        shop1.getComments().put(comment3.getCommentId(), comment3);
        shop2.getComments().put(comment4.getCommentId(), comment4);

        shop1.getProducts().put(product1.getProductId(), product1);
        shop1.getProducts().put(product2.getProductId(), product2);
        shop1.getProducts().put(product3.getProductId(), product3);
        shop2.getProducts().put(product1.getProductId(), product1);
        shop2.getProducts().put(product2.getProductId(), product2);
        shop3.getProducts().put(product3.getProductId(), product3);

        product1.getShops().put(shop1.getShopID(), shop1);
        product1.getShops().put(shop2.getShopID(), shop2);
        product2.getShops().put(shop1.getShopID(), shop1);
        product2.getShops().put(shop2.getShopID(), shop2);
        product3.getShops().put(shop1.getShopID(), shop1);
        product3.getShops().put(shop3.getShopID(), shop3);

    }
    */

    // Show all shops
    @GetMapping
    public String getAllShops(Model model) {
        List<Shop> shops = shopService.findAll();
        model.addAttribute("shops", shops);
        return "index"; // View showing all the shops
    }

    // View details of a shop by its ID
    @GetMapping("/shops/{shopID}")
    public String getShopById(@PathVariable("shopID") long shopId, Model model) {
        Optional<Shop> shop = shopService.findById(shopId);
        if (shop.isPresent()) {
            model.addAttribute("shop", shop);
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
                shopService.save(shop);
            }
            return "redirect:/";
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la tienda no puede ser vacío");
        }

    }
    /// PARA VER LA IMAGEN, NO SE SI SE USA
    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> viewImage(@PathVariable String imageName) throws MalformedURLException {
        Path imagePath = IMAGES_FOLDER.resolve(imageName + ".jpg");
        Resource image = new UrlResource(imagePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(image);
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
    public String newProductToShop(@RequestParam String productName, @RequestParam(required = false) Double productPrize, @PathVariable long shopID) {
        if(shopService.findById(shopID).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        if (productPrize == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio del producto no puede estar vacío");
        }
        if (productName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío");
        }
        Product product = new Product(productName, productPrize);
        productService.saveShopInProduct(product, shopService.findById(shopID).get());
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
    public String newCommentToShop(@RequestParam String user, @RequestParam String issue,@RequestParam String message, @PathVariable long shopID) {
        if (!user.trim().isEmpty()){
            Optional<Shop> shop = shopService.findById(shopID);
            if (shop.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
            }
            Optional<User> useraux = userService.findByName(user);
            if (useraux.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
            }
            Comment comment= new Comment(useraux.get(),issue,message);
            shopService.saveComment(shop.get(), comment);
            return "redirect:/shops/" + shopID;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del usuario no puede estar vacio");
    }
}

