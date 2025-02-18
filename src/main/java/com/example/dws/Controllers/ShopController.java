package com.example.dws.Controllers;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
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

    @PostConstruct
    public void init() {

        Shop shop1 = new Shop("Tienda1");
        Shop shop2 = new Shop("Tienda2");

        shopRepository.save(shop1);
        shopRepository.save(shop2);

        Product product1 = new Product("Producto 1", 20);
        Product product2 = new Product("Producto 2", 30);

        shopRepository.addProductToShop(1, product1);
        shopRepository.addProductToShop(1, product2);

        productRepository.addShopToProduct(1, shop1);
        productRepository.addShopToProduct(2, shop1);

        productRepository.save(product1);
        productRepository.save(product2);
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
            // Mostrar los productos de la tienda y sus atributos
            System.out.println("Productos de la tienda " + shop.getShopName() + ": ");
            shop.getProducts().forEach((productId, product) -> {
                System.out.println("Producto ID: " + product.getProductId());
                System.out.println("Nombre: " + product.getProductName());
                System.out.println("Precio: " + product.getProductPrize());
            });
            return "Shop"; // Vista que muestra los detalles de la tienda
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

    // Eliminar una tienda
    @PostMapping("/delete")
    public String deleteShop(@RequestParam long shopID) {
        shopRepository.deleteById(shopID); // Eliminar la tienda por su ID
        return "redirect:/"; // Redirige a la lista de tiendas
    }
}
