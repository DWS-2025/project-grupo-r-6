package com.example.dws.Controllers;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.CommentRepository;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import com.example.dws.Repositories.UserRepository;
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

@Controller
public class ShopController {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    private static final Path IMAGES_FOLDER = Paths.get("uploads");

    @PostConstruct
    public void init() {

        Shop shop1 = new Shop("PullAndCow", "Pull");
        Shop shop2 = new Shop("Bresh", "Bresh");

        shopRepository.save(shop1);
        shopRepository.save(shop2);

        Product product1 = new Product("Producto 1", 20);
        Product product2 = new Product("Producto 2", 30);

        productRepository.save(product1);
        productRepository.save(product2);

        User user1 = new User("Adri", "adri@gmail.com");
        userRepository.save(user1);
        Comment comment1 = new Comment(user1, "Atención Al Cliente", "Muy buena atencion al cliente");
        commentRepository.save(comment1);

        shop1.getComments().put(comment1.getCommentId(), comment1);

        shop1.getProducts().put(product1.getProductId(), product1);
        shop1.getProducts().put(product2.getProductId(), product2);
        shop2.getProducts().put(product2.getProductId(), product2);

        product1.getShops().put(shop1.getShopID(), shop1);
        product2.getShops().put(shop1.getShopID(), shop1);
        product2.getShops().put(shop2.getShopID(), shop2);

    }


    // Mostrar todas las tiendas
    @GetMapping
    public String getAllShops(Model model) {
        Collection<Shop> shops = shopRepository.findAll();
        model.addAttribute("shops", shops);
        return "index"; // Vista que muestra todas las tiendas
    }

    // Ver detalles de una tienda por su ID
    @GetMapping("/shops/{shopID}")
    public String getShopById(@PathVariable("shopID") long shopId, Model model) {
        Shop shop = shopRepository.findById(shopId);
        if (shop != null) {
            model.addAttribute("shop", shop);
            return "showShop"; // Vista que muestra los detalles de la tienda
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
    }


    // Guardar una nueva tienda
    @PostMapping("/save")
    public String saveShop(@RequestParam String shopName,
                           @RequestParam String imageName,
                           @RequestParam MultipartFile image) throws IOException {
        if (shopName != null && !shopName.trim().isEmpty()) {
            if(!image.isEmpty() && imageName.trim().isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Si quieres añadir una imagen el nobre de la imagen no puede ser vacío");
            } else {
                // Crear directorio si no existe
                Files.createDirectories(IMAGES_FOLDER);

                // Guardar la imagen
                Path imagePath = IMAGES_FOLDER.resolve(imageName + ".jpg");
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                // Crear y guardar la tienda en el repository
                Shop shop = new Shop(shopName, imageName);
                shopRepository.save(shop);
            }

            return "redirect:/";
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la tienda no puede ser vacío");
        }

    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> viewImage(@PathVariable String imageName) throws MalformedURLException {
        Path imagePath = IMAGES_FOLDER.resolve(imageName + ".jpg");
        Resource image = new UrlResource(imagePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(image);
    }

    // Borra la tienda de los productos que la tienen asignada y se borra la tienda
    @PostMapping("/delete")
    public String deleteShop(@RequestParam long shopID) {
        if(shopRepository.findById(shopID) == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        } else{
            productRepository.removeShopFromAllProducts(shopID);
            shopRepository.deleteById(shopID); // Eliminar la tienda por su ID
            return "redirect:/"; // Redirige a la lista de tiendas
        }
    }

    @PostMapping("/shops/{shopID}/products/new")
    public String newProductToShop(@RequestParam String productName, @RequestParam(required = false) Double productPrize, @PathVariable long shopID) {
        if(shopRepository.findById(shopID) == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        if (productPrize == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio del producto no puede estar vacío");
        }
        if (productName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío");
        }
        Product product = new Product(productName, productPrize);
        Shop shop = shopRepository.findById(shopID);
        shop.getProducts().put(product.getProductId(), product);
        product.getShops().put(shopID, shop);
        productRepository.save(product);
        shopRepository.save(shop);
        return "redirect:/shops/" + shopID;
    }

    @PostMapping("/shops/{shopID}/comments/new")
    public String newCommentToShop(@RequestParam String user, @RequestParam String issue,@RequestParam String message, @PathVariable long shopID) {
        if (!user.trim().isEmpty()){
            Shop shop = shopRepository.findById(shopID);
            if (shop == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
            }
            User useraux = userRepository.findByName(user);
            if (useraux == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
            }
            Comment comment= new Comment(useraux,issue,message);
            shop.getComments().put(comment.getCommentId(), comment);
            commentRepository.save(comment);
            shopRepository.save(shop);
            return "redirect:/shops/" + shopID;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del usuario no puede estar vacio");
    }
}

