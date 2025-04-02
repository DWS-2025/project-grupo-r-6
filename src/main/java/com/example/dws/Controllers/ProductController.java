package com.example.dws.Controllers;


import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.DTOs.UserDTO;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import com.example.dws.Repositories.UserRepository;
import com.example.dws.Service.ProductService;
import com.example.dws.Service.ShopService;
import com.example.dws.Service.UserService;
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
import java.util.List;
import java.util.Optional;


@Controller
public class ProductController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping("/products/{productID}")
    public String getProductById(@PathVariable("productID") long productID,Model model) {
        Optional<ProductDTO> productDTO = productService.findById(productID);
        if (productDTO.isPresent()) {
            model.addAttribute("product", productDTO);
            List<ShopDTO> shops = shopService.findAll();
            model.addAttribute("shops", shops);
            return "showProduct"; // View showing shop details
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El prodcuto seleccionado no existe");// Error view if shop not found
        }
    }

    @PostMapping("/products/{productID}/addShop/")
    public String addShopToProduct(@PathVariable("productID") long productID, long shopID) {
        Optional<ProductDTO> productDTO = productService.findById(productID);
        if (productDTO.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
        Optional<ShopDTO> shopDTO = shopService.findById(shopID);
        if(shopDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        productService.saveShopInProduct(productDTO.get(), shopDTO.get());
        return "redirect:/products/" + productID;
    }

    // Delete product from the shops that have it and delete it from the repository
    @PostMapping("/products/{productID}/delete")
    public String deleteProduct(@PathVariable("productID") long productID){
        Optional<ProductDTO> productDTO = productService.findById(productID);
        if(productDTO.isPresent()){
            shopService.removeProductFromAllShops(productDTO.get());
            productService.deleteById(productID);
            return "deleted_product";
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
    }
    // A user purchases a product and adds it to their cart
    @PostMapping("/products/{productID}/buy")
    public String buyProduct(@PathVariable("productID") long productID, @RequestParam("username") String username){
        Optional<ProductDTO> productDTO = productService.findById(productID);
        if(productDTO.isPresent()){
            Optional<UserDTO> userDTO = userService.findByName(username);
            if(userDTO.isPresent()){
                userService.addProduct(userDTO.get(), productDTO.get());
                return "redirect:/users/" + userService.getId(userDTO.get());
            } else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario seleccionado no existe");
            }
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }

    }
    @PostMapping("/products/{productId}/update/")
    public String updateProduct(ProductDTO productDTO, @PathVariable long productId,Model model ){
        Optional<ProductDTO> product = productService.findById(productId);
        if (product.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
        if (productDTO.productPrize()== null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio del producto no puede estar vacío");
        }
        if (productDTO.productName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío");
        }

        this.productService.update(productId,productDTO);
        model.addAttribute("product", product);
        return "updated_product";
    }





}
