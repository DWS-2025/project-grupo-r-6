package com.example.dws.Controllers;


import com.example.dws.DTOs.*;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import com.example.dws.Repositories.UserRepository;
import com.example.dws.Service.FileStorageService;
import com.example.dws.Service.ProductService;
import com.example.dws.Service.ShopService;
import com.example.dws.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private FileStorageService fileStorageService;
    @Autowired
    private UserService userService;

    @GetMapping("/products/{productID}")
    public String getProductById(@PathVariable("productID") long productID,Model model) {
        Optional<ProductDTO> productDTO = productService.findById(productID);
        if (productDTO.isPresent()) {
            model.addAttribute("product", productDTO.get());
            List<ShopBasicDTO> shops = productDTO.get().shops();
            model.addAttribute("shops", shops);
            List<ShopDTO> allShops = shopService.findAll();
            model.addAttribute("allShops", allShops);
            boolean hasFile = false;
            String originalFileName = null;

            try {
                originalFileName = fileStorageService.getOriginalFilename(productDTO.get().productId());
                Path filePath = Paths.get(fileStorageService.getFileStorageLocation().toString(),
                        productDTO.get().productId() + "_" + originalFileName);
                if (Files.exists(filePath)) {
                    hasFile = true;
                    model.addAttribute("fileName", originalFileName);
                }
            } catch (Exception e){
            }
            return "showProduct"; // View showing shop details
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El prodcuto seleccionado no existe");// Error view if shop not found
        }
    }

    @GetMapping("/products/{productID}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long productID) {
        try {
            Resource resource = fileStorageService.loadFileAsResource(productID);
            String originalFileName = fileStorageService.getOriginalFilename(productID);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo no encontrado");
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
        boolean alreadyAdded = false;
        for (ProductBasicDTO p : shopDTO.get().products()) {
            if (p.productId() == productID) {
                alreadyAdded = true;
                break;
            }
        }
        if (!alreadyAdded) {
            productService.saveProductInShop(productDTO.get(), shopDTO.get());
        }
        return "redirect:/products/" + productID;
    }

    // Delete product from the shops that have it and delete it from the repository
    @PostMapping("/products/{productID}/delete")
    public String deleteProduct(@PathVariable("productID") long productID){
        Optional<ProductDTO> productDTO = productService.findById(productID);
        if(productDTO.isPresent()){
            shopService.removeProductFromAllShops(productDTO.get());
            userService.removeProductFromUser(productDTO.get());
            productService.deleteById(productID);
            return "deleted_product";
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
    }
    // A user purchases a product and adds it to their cart
    @PostMapping("/products/{productID}/buy")
    public String buyProduct(@PathVariable("productID") long productID){
        Optional<ProductDTO> productDTO = productService.findById(productID);
        if(productDTO.isPresent()){
            Long id = userService.addProduct(productDTO.get());
            return "redirect:/users/" + id;
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }

    }
    @PostMapping("/products/{productId}/update/")
    public String updateProduct(ProductDTO productDTO, @PathVariable long productId,Model model ){
        Optional<ProductDTO> product = productService.findById(productId);
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
        if (productDTO.productPrize()== null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio del producto no puede estar vacío");
        }
        if (productDTO.productName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío");
        }

        this.productService.update(productId,productDTO);
        model.addAttribute("product", product.get());
        return "updated_product";
    }

    @GetMapping("/productSearch")
    public String showProducts(Model model, HttpServletRequest request,
                               @RequestParam(name = "from", required = false) Integer from,
                               @RequestParam(name = "to", required = false) Integer to,
                               @RequestParam(name = "name", required = false) String name) {

        List<Product> productList = productService.findProductsByNameAndPrice(from, to, name);
        model.addAttribute("products", productList);
        return "products";
    }






}
