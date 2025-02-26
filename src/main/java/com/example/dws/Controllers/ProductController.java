package com.example.dws.Controllers;


import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import com.example.dws.Repositories.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

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

        if (!product.getShops().containsKey(shopID)){
            product.getShops().put(shopID, shop) ;
            shop.getProducts().put(productID, product) ;
        }

        productRepository.save(product);
        shopRepository.save(shop);

        return "redirect:/products/" + productID;
    }

    // Borra el producto de las tiendas que lo tengan y se borra del repository
    @PostMapping("/products/{productID}/delete")
    public String deleteProduct(@PathVariable("productID") long productID){
        Product product = productRepository.findById(productID);
        if(product != null){
            shopRepository.removeProductFromAllShops(productID);
            productRepository.deleteById(productID);
            return "deleted_product";
        } else{
            return "product_not_found";
        }
    }
    // Un usuario compra un producto y se añade a su carrito
    @PostMapping("/products/{productID}/buy")
    public String buyProduct(@PathVariable("productID") long productID, @RequestParam("username") String username){
        Product product = productRepository.findById(productID);
        if(product != null){
            User user = userRepository.findByName(username);
            if(user != null){
                user.addProduct(productID, product);
                return "redirect:/users/" + user.getId();
            } else{
                return "user_not_found";
            }
        } else{
            return "product_not_found";
        }

    }
    @PostMapping("/products/{productId}/update/")
    public String updateProduct(Product product, @PathVariable long productId,Model model ){
        Product actual = this.productRepository.findById(productId);
        actual.setProductName(product.getProductName());
        actual.setProductPrize(product.getProductPrize());
        this.productRepository.save(actual);
        model.addAttribute("product", actual);
        return "updated_product";
    }





}
