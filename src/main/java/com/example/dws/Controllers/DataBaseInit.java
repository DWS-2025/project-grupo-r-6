
package com.example.dws.Controllers;

import com.example.dws.DTOs.GeneralMapper;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.DTOs.UserDTO;
import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.ShopRepository;
import com.example.dws.Service.CommentService;
import com.example.dws.Service.ProductService;
import com.example.dws.Service.ShopService;
import com.example.dws.Service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
public class DataBaseInit implements CommandLineRunner {

    @Autowired
    private ShopService shopService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private GeneralMapper generalMapper;

    @Autowired
    private ShopRepository shopRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Shop shop1=new Shop("PullAndCow", "Pull");
        Shop shop2=new Shop("Bresh", "Bershka");
        Shop shop3=new Shop("Zaro", "Zara");

        shopService.saveShopWithImage(shop1, "Pull.jpg");
        shopService.saveShopWithImage(shop2, "Bresh.jpg");
        shopService.saveShopWithImage(shop3, "Zaro.jpg");
/*
        shopService.save(generalMapper.shopToShopDTO(shop1));
        shopService.save(generalMapper.shopToShopDTO(shop2));
        shopService.save(generalMapper.shopToShopDTO(shop3));

 */

        Product product1=new Product("Camiseta", 20);
        Product product2=new Product("Sudadera", 35);
        Product product3=new Product("Zapatillas", 62.99);
        ProductDTO productDTO1 = generalMapper.productToProductDTO(product1);
        ProductDTO productDTO2 = generalMapper.productToProductDTO(product2);
        ProductDTO productDTO3 = generalMapper.productToProductDTO(product3);

        productService.save(productDTO1);
        productService.save(productDTO2);
        productService.save(productDTO3);

        User user = new User("Adri", "adri@gmail.com");
        UserDTO userDTO = generalMapper.userToUserDTO(user);
        userService.save(userDTO);

        UserDTO userDTO1 = userService.findByName("Adri").get();
        User user1 = generalMapper.userDTOToUser(userDTO1);

        productService.saveProductInShop(productService.findByName("Camiseta").get(), shopService.findByName("PullAndCow").get());
        productService.saveProductInShop(productService.findByName("Sudadera").get(), shopService.findByName("PullAndCow").get());
        productService.saveProductInShop(productService.findByName("Zapatillas").get(), shopService.findByName("PullAndCow").get());
        productService.saveProductInShop(productService.findByName("Camiseta").get(), shopService.findByName("Bresh").get());
        productService.saveProductInShop(productService.findByName("Sudadera").get(), shopService.findByName("Bresh").get());
        productService.saveProductInShop(productService.findByName("Camiseta").get(), shopService.findByName("Zaro").get());
        
        Shop shop = generalMapper.shopDTOToShop(shopService.findByName("PullAndCow").get());
        Comment comment= new Comment(user1, "Atención Al Cliente", "Tuve una excelente experiencia con el servicio al cliente. Fueron muy amables y me ayudaron a encontrar lo que buscaba. ¡Muy satisfecho!");
        shop.getComments().add(comment);
        commentService.save(comment);
        shopService.save(generalMapper.shopToShopDTO(shop));

    }

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
}


