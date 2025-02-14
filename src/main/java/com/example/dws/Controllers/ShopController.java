package com.example.dws.Controllers;

import com.example.dws.Entities.*;
import com.example.dws.Enums.ShopType;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;

@Controller
public class ShopController {
    @Autowired
    private ShopRepository shoprepository;
    @Autowired
    private ProductRepository productrepository;
    @Autowired
    private CitiesRepository cityrepository;
    @GetMapping({"/shops"})
    public String shops(Model model) {
        model.addAttribute("shop", this.shoprepository.returnMyShops());
        return "shops";
    }
    @GetMapping({"/shopForm"})
    public String shopsForm(Model model) {
        Collection<City> cities = cityrepository.returnMyCities();
        model.addAttribute("cities", cities);
        return "shopForm";
    }
    @GetMapping({"/shopCityForm"})
    public String shopsCity(Model model, @RequestParam Long cityId) {
        model.addAttribute("id", cityId);
        return "shopCityForm";
    }

    @GetMapping({"/addShop"})
    public String getShopForm(Model model, @RequestParam ShopType type, @RequestParam int extension, @RequestParam int workersNum, @RequestParam List<Long> selectedCities) {
            Shop shop = new Shop(type, extension, workersNum);
            for (Long id : selectedCities) {
                City city = this.cityrepository.getCitybyID(id);
                city.addShopType(shop.getId(), shop.getType());
                shop.addCity(city);
            }
            this.shoprepository.putShopsMap(shop);

        return "redirect:/shops";
    }

    @GetMapping("/removeShop")
    public String removeShop(Model model,@RequestParam Long id){
        this.shoprepository.removeShopProducts(id);
        this.shoprepository.removeShop(id);
        return "redirect:/shops";
    }

    @GetMapping("/removeShopForm")
    public String removeShop(Model model){
        return "removeShopForm";
    }

    @GetMapping({"/addShop/addObject/{id}"})
    public String getClothesForm(Model model, @RequestParam Type type, @RequestParam Size size, @RequestParam int prize, @RequestParam String brand, @PathVariable Long id, RedirectAttributes attributes) {
        model.addAttribute("shopId", id);
        Product product = new Product(type, size, (double)prize, brand);
        this.productrepository.addProduct(product);
        Shop shop = shoprepository.getShopById(id);
        product.putMap(shop, id);
        this.shoprepository.putProductsMap(product, id);
        return "redirect:/shopManagement/{id}";
    }
    @GetMapping({"/addShop/removeObject/{shopId}"})
    public String getRemoveClothesForm(Model model, @RequestParam Long id, @PathVariable Long shopId) {
        this.shoprepository.removeProduct(id, shopId);
        return "redirect:/shopManagement/{shopId}";
    }

    @GetMapping({"/updateShop"})
    public String getUpdateShopForm(Model model, @RequestParam Long id) {
        return "redirect:/formShopUpdate/"+id;
    }

    @GetMapping({"/formShopUpdate/{id}"})
    public String UpdateShopForm(Model model,@PathVariable Long id) {
        Collection<City> cities = cityrepository.returnMyCities();
        model.addAttribute("cities", cities);
        model.addAttribute("index",id);
        return "updateShop";
    }


    @GetMapping({"/updateShopCorrect/{index}"})
    public String RecibirFormularioActualizarTienda(Model model,@PathVariable Long index,@RequestParam ShopType type,@RequestParam int extension,@RequestParam int workersNum,@RequestParam List<Long> selectedCities) {
        Shop shop = new Shop(type, extension, workersNum);
        for(Long id:selectedCities){
            City city = this.cityrepository.getCitybyID(id);
            city.addShopType(shop.getId(), shop.getType());
            shop.addCity(city);
        }
        this.shoprepository.updateShop(shop,index);
        return "redirect:/shops";
    }

    @GetMapping({"/formUpdateShopId"})
    public String formUpdateShop(Model model) {
        return "selectIdForUpdateShow";
    }

    @GetMapping({"/shopManagement/{index}"})
    public String specificStore(Model model, @PathVariable Long index) {
        model.addAttribute("shop", this.shoprepository.showShops().get(index));
        model.addAttribute("indexShop", index);
        if (((Shop)this.shoprepository.showShops().get(index)).showProducts() != null) {
            model.addAttribute("products", ((Shop)this.shoprepository.showShops().get(index)).showProducts());
        }
        return "management";
    }
}
