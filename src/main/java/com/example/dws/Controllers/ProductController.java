package com.example.dws.Controllers;


import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class ProductController {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products/{productID}")
    public String getShopById(@PathVariable("productId") long shopId, Model model) {
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
            return "showShop"; // Vista que muestra los detalles de la tienda
        } else {
            return "error"; // Vista de error si no se encuentra la tienda
        }
    }
}
