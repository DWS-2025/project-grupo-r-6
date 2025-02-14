package com.example.dws.Controllers;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.Size;
import com.example.dws.Entities.Type;
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
    private ShopRepository shoprepository;
    @Autowired
    private ProductRepository productrepository;
    @Autowired
    private CitiesRepository cityrepository;
    @GetMapping({"/products"})
    public String products(Model model) {
        model.addAttribute("product", this.productrepository.returnMyProducts());
        return "cities";
    }
    @GetMapping({"/clothesForm"})
    public String clothesShop(Model model, @RequestParam Long shopId) {
        model.addAttribute("id", shopId);
        return "clothesForm";
    }
    @GetMapping({"/removeClothesForm"})
    public String FormularioEliminarRopa(Model model, @RequestParam Long shopId) {
        model.addAttribute("shopId", shopId);
        return "removeClothesForm";
    }

    @GetMapping({"/addProduct"})
    public String addProduct(Model model, @RequestParam Type type, @RequestParam Size size, @RequestParam double prize, @RequestParam String brand, @RequestParam String[] shopsIds) {
        Product product = new Product(type, size, prize, brand);
        for (String shopId : shopsIds){
            Long id = Long.parseLong(shopId);
            Shop shop = shoprepository.getShopById(id);
            if(shop != null){
                product.putMap(shop, id);
                this.shoprepository.putProductsMap(product, id);
            }
        }
        this.productrepository.putProductsMap(product);
        return "redirect:/products";
    }
    @GetMapping({"/formUpdateClothesId"})
    public String formUpdateClothes(Model model, @RequestParam Long shopId) {
        model.addAttribute("indexShop", shopId);
        return "selectIdForUpdateItem";
    }
    @GetMapping({"/updateObject/{indexShop}"})
    public String formUpdateClothes2(Model model, @PathVariable Long indexShop, @RequestParam Long id) {
        return "redirect:/formUpdateClothes3/"+indexShop+'/'+id;
    }

    @GetMapping({"/formUpdateClothes3/{indexShop}/{id}"})
    public String formUpdateClothes3(Model model,@PathVariable Long indexShop,@PathVariable Long id) {
        model.addAttribute("indexShop", indexShop);
        model.addAttribute("indexProduct", id);

        return "updateItem";
    }

    @GetMapping({"/updateObject/{indexShop}/{id}"})
    public String ActualizarRopa(Model model,@PathVariable Long indexShop,@PathVariable Long id,@RequestParam Type type, @RequestParam Size size, @RequestParam int prize, @RequestParam String brand) {
        Product product=new Product(type,size,prize,brand);
        this.shoprepository.updateObject(product,indexShop,id);

        return "redirect:/shopManagement/{indexShop}";
    }
}
