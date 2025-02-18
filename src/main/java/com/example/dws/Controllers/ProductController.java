package com.example.dws.Controllers;


import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;


@Controller
public class ProductController {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products/{productID}")
    public String getProductById(@PathVariable("productID") long productID, Model model) {
        Product product = productRepository.findById(productID);
        if (product != null) {
            model.addAttribute("product", product);
            Collection<Shop> shops = shopRepository.findAll();
            model.addAttribute("shops", shops);
            return "showProduct"; // Vista que muestra los detalles de la tienda
        } else {
            return "error"; // Vista de error si no se encuentra la tienda
        }
    }

    @PostMapping("/products/{productID}/addShop/")
    public String addShopToProduct(@PathVariable("productID") long productID, long shopID) {
        Product product = productRepository.findById(productID);
        Shop shop = shopRepository.findById(shopID);

        if (!product.getShops().containsKey(shopID)) product.getShops().put(shopID, shop);

        productRepository.save(product);

        return "redirect:/products/" + productID;
    }

}
