package com.example.dws.Controllers;

import com.example.dws.Entities.*;
import com.example.dws.Enums.ShopType;
import com.example.dws.Repositories.CitiesRepository;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.concurrent.atomic.AtomicLong;

@Controller
public class GeneralController {

    @Autowired
    private ShopRepository shoprepository;
    @Autowired
    private ProductRepository productrepository;
    @Autowired
    private CitiesRepository cityrepository;

    @PostConstruct
    public void initializeShopsandCities() {
        // Inicializa las tiendas solo una vez
        Shop Bresh = new Shop(ShopType.Bresh, 10, 6);
        Shop PullandCow = new Shop(ShopType.PullAndCow, 16, 9);
        City Lisboa = new City("Lisboa", "Portugal");
        City Madrid = new City("Madrid", "Espana");
        City Paris = new City("Paris", "Francia");
        Madrid.setImageFileName("madrid.jpg");
        Lisboa.setImageFileName("lisboa.jpeg");
        Paris.setImageFileName("paris.jpg");
        Lisboa.addShopType(Bresh.getId(), Bresh.getType());
        Bresh.addCity(Lisboa);
        this.shoprepository.putShopsMap(Bresh);
        this.cityrepository.putCity(Lisboa);
        Madrid.addShopType(PullandCow.getId(), PullandCow.getType());
        Paris.addShopType(PullandCow.getId(), PullandCow.getType());
        PullandCow.addCity(Lisboa);
        PullandCow.addCity(Paris);
        this.shoprepository.putShopsMap(PullandCow);
        this.cityrepository.putCity(Madrid);
        this.cityrepository.putCity(Paris);
    }


    @GetMapping({"/index"})
    public String board(Model model) {
        model.addAttribute("counter", this.shoprepository.getShopCounter());
        AtomicLong lastID = new AtomicLong();
        model.addAttribute("ShopID",lastID);
        model.addAttribute("id",lastID);
        return "index";
    }

    @GetMapping({"/error"})
    public String error() {
        return "error";
    }

    @GetMapping({"/service"})
    public String service(Model model) {
        model.addAttribute("Shop", this.shoprepository.returnMyShops());
        return "service";
    }
    @GetMapping({"/myObjects/{indexShop}/{index}"})
    public ModelAndView MyObjects(Model model,@PathVariable Long indexShop,@PathVariable Long index) {
        if(indexShop!=0&&index!=0){
            Product product=this.shoprepository.showProduct(indexShop,index);
            if (product == null){
                return new ModelAndView(new RedirectView("http://localhost:8080/myObjects/0/0"));
            }
            this.shoprepository.removeProduct(index,indexShop);
        }
        ModelAndView modelAndView = new ModelAndView("myObjects");
        modelAndView.addObject("product", this.productrepository.getAllProducts());
        return modelAndView;

    }

}
