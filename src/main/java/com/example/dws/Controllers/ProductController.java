package com.example.dws.Controllers;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Enums.Size;
import com.example.dws.Enums.Type;
import com.example.dws.Repositories.CitiesRepository;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CitiesRepository cityrepository;

    // Mostrar todos los productos
    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productRepository.getAllProducts());
        return "cities";
    }

    // Formulario para agregar ropa a una tienda
    @GetMapping("/clothesForm")
    public String clothesShop(Model model, @RequestParam Long shopId) {
        model.addAttribute("shopId", shopId);
        return "clothesForm";
    }

    // Formulario para eliminar ropa
    @GetMapping("/removeClothesForm")
    public String FormularioEliminarRopa(Model model, @RequestParam Long shopId) {
        model.addAttribute("shopId", shopId);
        return "removeClothesForm";
    }

    // Agregar un producto a la tienda
    @GetMapping({"/addProduct"})
    public String addProduct(Model model, @RequestParam Type type, @RequestParam Size size, @RequestParam double prize, @RequestParam String brand, @RequestParam String[] shopsIds) {
        Product product = new Product(type, size, prize, brand);
        for (String shopId : shopsIds){
            Long id = Long.parseLong(shopId);
            Shop shop = shopRepository.getShopById(id);
            if(shop != null){
                product.putMap(shop, id);
                this.shopRepository.putProductsMap(product, id);
            }
        }
        this.productRepository.addProduct(product);
        return "redirect:/products";
    }

    // Formulario para seleccionar tienda a actualizar
    @GetMapping({"/formUpdateClothesId"})
    public String formUpdateClothes(Model model, @RequestParam Long shopId) {
        model.addAttribute("shopId", shopId);
        return "selectIdForUpdateItem";
    }

    // Formulario para actualizar producto
    @GetMapping("/updateObject/{shopId}")
    public String formUpdateClothes2(@PathVariable Long shopId, @RequestParam Long productId) {
        return "redirect:/formUpdateClothes3/" + shopId + '/' + productId;
    }

    // Formulario para actualizar producto - Vista final
    @GetMapping("/formUpdateClothes3/{shopId}/{productId}")
    public String formUpdateClothes3(Model model, @PathVariable Long shopId, @PathVariable Long productId) {
        model.addAttribute("shopId", shopId);
        model.addAttribute("productId", productId);
        return "updateItem";
    }

    // Actualizar producto en tienda
    @GetMapping("/updateObject/{shopId}/{productId}")
    public String actualizarRopa(
            @PathVariable Long shopId,
            @PathVariable Long productId,
            @RequestParam Type type,
            @RequestParam Size size,
            @RequestParam double price,
            @RequestParam String brand) {

        Product product = new Product(type, size, price, brand);
        shopRepository.updateObject(product, shopId, productId);

        return "redirect:/shopManagement/" + shopId;
    }
}
