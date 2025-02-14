package com.example.dws.Controllers;

import com.example.dws.Entities.City;
import com.example.dws.Entities.Shop;
import com.example.dws.Enums.ShopType;
import com.example.dws.Repositories.CitiesRepository;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class CityController {
    @Autowired
    private ShopRepository shoprepository;
    @Autowired
    private ProductRepository productrepository;
    @Autowired
    private CitiesRepository cityrepository;

    @GetMapping({"/cities"})
    public String cities(Model model) {
        model.addAttribute("city", this.cityrepository.returnMyCities());
        return "cities";
    }

    @GetMapping({"/cityForm"})
    public String cityForm(Model model) {
        return "citiesForm";
    }

    @GetMapping({"/cityManagement/{index}"})
    public String specificCity(Model model, @PathVariable Long index) {
        model.addAttribute("city", this.cityrepository.getCitybyID(index));
        model.addAttribute("indexCity", index);
        if ((this.cityrepository.getCitybyID(index).showShops() != null)) {
            model.addAttribute("shops", (this.cityrepository.getCitybyID(index).showShops()));
        }
        return "citymanagement";
    }
    @PostMapping({"/addCity"})
    public String addCity(Model model, @RequestParam String name, @RequestParam String country, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (name.isEmpty()){
            return "redirect:/emptyCityName";
        } else {

        City city = new City(name, country);

        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());

        String uploadDir = "src/main/resources/static/img";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (FileOutputStream fos = new FileOutputStream(uploadDir + File.separator + fileName)) {
                fos.write(imageFile.getBytes());
            }

            city.setImageFileName(fileName);
            this.cityrepository.putCity(city);
        return "redirect:/cities";
        }
    }

    @GetMapping({"/addCity/addShop/{id}"})
    public String getShopCityForm(Model model, @RequestParam ShopType type, @RequestParam int extension, @RequestParam int workersNum, @PathVariable Long id) {
        model.addAttribute("cityId", id);
        Shop shop = new Shop(type, extension, workersNum);
        if(!this.shoprepository.containsShop(shop.getType())){
            this.shoprepository.putShopsMap(shop);
        }
        this.cityrepository.getCitybyID(id).putMap(shop, shop.getId());
        return "redirect:/cityManagement/{id}";
    }
}
