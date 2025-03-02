package com.example.dws.Controllers;


import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import com.example.dws.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

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
            return "showProduct"; // View showing shop details
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El prodcuto seleccionado no existe");// Error view if shop not found
        }
    }

    @PostMapping("/products/{productID}/addShop/")
    public String addShopToProduct(@PathVariable("productID") long productID, long shopID) {
        Product product = productRepository.findById(productID);
        if (product == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El prodcuto seleccionado no existe");
        }
        Shop shop = shopRepository.findById(shopID);
        if(shop == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }

        if (!product.getShops().containsKey(shopID)){
            product.getShops().put(shopID, shop) ;
            shop.getProducts().put(productID, product) ;
        }

        productRepository.save(product);
        shopRepository.save(shop);

        return "redirect:/products/" + productID;
    }

    // Delete product from the shops that have it and delete it from the repository
    @PostMapping("/products/{productID}/delete")
    public String deleteProduct(@PathVariable("productID") long productID){
        Product product = productRepository.findById(productID);
        if(product != null){
            shopRepository.removeProductFromAllShops(productID);
            productRepository.deleteById(productID);
            return "deleted_product";
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
    }
    // A user purchases a product and adds it to their cart
    @PostMapping("/products/{productID}/buy")
    public String buyProduct(@PathVariable("productID") long productID, @RequestParam("username") String username){
        Product product = productRepository.findById(productID);
        if(product != null){
            User user = userRepository.findByName(username);
            if(user != null){
                user.addProduct(productID, product);
                return "redirect:/users/" + user.getId();
            } else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
            }
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }

    }
    @PostMapping("/products/{productId}/update/")
    public String updateProduct(@RequestParam String productName, @RequestParam(required = false) Double productPrize, @PathVariable long productId,Model model ){
        Product product = this.productRepository.findById(productId);
        if (product == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
        if (productPrize == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio del producto no puede estar vacío");
        }
        if (productName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío");
        }
        product.setProductName(productName);
        product.setProductPrize(productPrize);
        this.productRepository.save(product);
        model.addAttribute("product", product);
        return "updated_product";
    }





}
