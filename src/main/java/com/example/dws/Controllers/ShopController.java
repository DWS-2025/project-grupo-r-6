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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

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

    @PostConstruct
    public void init() {

        Shop shop1 = new Shop("Tienda1");
        Shop shop2 = new Shop("Tienda2");

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
    @GetMapping("/shops/{shopId}")
    public String getShopById(@PathVariable("shopId") long shopId, Model model) {
        Shop shop = shopRepository.findById(shopId);
        if (shop != null) {
            model.addAttribute("shop", shop);
            return "showShop"; // Vista que muestra los detalles de la tienda
        } else {
            return "error"; // Vista de error si no se encuentra la tienda
        }
    }

    // Crear una nueva tienda (Formulario)
    @GetMapping("/new")
    public String createShopForm(Model model) {
        model.addAttribute("shop", new Shop("")); // Inicializar un objeto Shop vacío
        return "shopForm"; // Vista con el formulario para crear una tienda
    }

    // Guardar una nueva tienda
    @PostMapping("/save")
    public String saveShop(@ModelAttribute Shop shop) {
        shopRepository.save(shop); // Guardamos la tienda en el repositorio
        return "redirect:/"; // Redirige a la lista de tiendas
    }

    // Borra la tienda de los productos que la tienen asignada y se borra la tienda
    @PostMapping("/delete")
    public String deleteShop(@RequestParam long shopID) {
        productRepository.removeShopFromAllProducts(shopID);
        shopRepository.deleteById(shopID); // Eliminar la tienda por su ID
        return "redirect:/"; // Redirige a la lista de tiendas
    }
    @PostMapping("/shops/{shopID}/products/new")
    public String newProductToShop(Product product, @PathVariable long shopID){
        Shop shop = shopRepository.findById(shopID);
        shop.getProducts().put(product.getProductId(), product);
        product.getShops().put(shopID, shop);
        productRepository.save(product);
        shopRepository.save(shop);
        return "redirect:/shops/" + shopID;
    }
}
