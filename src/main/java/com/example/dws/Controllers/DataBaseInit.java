package com.example.dws.Controllers;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.User;
import com.example.dws.Service.CommentService;
import com.example.dws.Service.ProductService;
import com.example.dws.Service.ShopService;
import com.example.dws.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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


    @Override
    public void run(String... args) throws Exception {
        Shop shop1=new Shop("PullAndCow", "Pull");
        Shop shop2=new Shop("Bresh", "Bershka");
        Shop shop3=new Shop("Zaro", "Zara");

        shopService.save(shop1);
        shopService.save(shop2);
        shopService.save(shop3);

        Product product1=new Product("Camiseta", 20);
        Product product2=new Product("Sudadera", 35);
        Product product3=new Product("Zapatillas", 62.99);
        productService.save(product1);
        productService.save(product2);
        productService.save(product3);

        User user = new User("Adri", "adri@gmail.com");
        userService.save(user);

        Comment comment= new Comment(user, "Atención Al ClienteA", "Tuve una excelente experiencia con el servicio al cliente. Fueron muy amables y me ayudaron a encontrar lo que buscaba. ¡Muy satisfecho!", shop1 );
        commentService.save(comment);

        shopService.saveProduct(shop1, product1);
        shopService.saveProduct(shop2, product2);
        shopService.saveProduct(shop3, product3);
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
