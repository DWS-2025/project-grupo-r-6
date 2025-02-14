package com.example.dws.Controllers;

import com.example.dws.Entities.City;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Enums.ShopType;
import com.example.dws.Repositories.CitiesRepository;
import com.example.dws.Repositories.ProductRepository;
import com.example.dws.Repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private ShopRepository shoprepository;
    @Autowired
    private ProductRepository productrepository;
    @Autowired
    private CitiesRepository citiesRepository;

    public RestController() {
        this.shoprepository = new ShopRepository();
    }
    public RestController(ShopRepository shoprepository){
        this.shoprepository = shoprepository;
    }

    @GetMapping("/api/shops/")
    public ResponseEntity<Collection<Shop>> getAllShops() {
        Collection<Shop> shops = shoprepository.returnMyShops();
        return new ResponseEntity<>(shops, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/shops/")
    public Shop createShop(@RequestBody Map<String, Object> requestData) {
        String type = (String) requestData.get("type");
        int extension = Integer.parseInt((String) requestData.get("extension"));
        int workersNum = Integer.parseInt((String) requestData.get("workersNum"));
        List<String> cityNames = (List<String>) requestData.get("cities");

        List<City> cities = new ArrayList<>();
        for (String cityName : cityNames) {
            City city = citiesRepository.getCityByName(cityName);
            if (city != null) {
                cities.add(city);
            }
        }
        Shop shop = new Shop(ShopType.valueOf(type), extension, workersNum);
        for (City city : cities) {
            city.addShopType(shop.getId(), shop.getType());
            shop.addCity(city);
        }
        this.shoprepository.putShopsMap(shop);
        return shop;

    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/cities/")
    public ResponseEntity<?> createCity(@RequestBody City city) {
        if(city.getName() == null || city.getName().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre de la ciudad no puede ser vacio");
        }
        this.citiesRepository.putCity(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(city);
    }

    @PostMapping("/api/cities/{id}/image/")
    public ResponseEntity<Object> uploadImage(@PathVariable long id,
                                              @RequestParam MultipartFile imageFile) throws IOException {
        City city = this.citiesRepository.findById(id);

        if(city != null){
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Archivo de imagen vacío");
            }

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

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @DeleteMapping({"/api/shops/{id2}/"})
    public ResponseEntity<Shop> removeShopByID(@PathVariable Long id2) {
        if (this.shoprepository.isShop(id2)) {
            this.shoprepository.removeShop(id2);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping({"/api/cities/{id2}/"})
    public ResponseEntity<City> removeCityByID(@PathVariable Long id2) {
        City c = citiesRepository.getCitybyID(id2);
        if (this.citiesRepository.isCity(id2)) {
            if (c.containsShops()) {
                c.deleteShops();
            }
            this.citiesRepository.removeCity(id2);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping({"/api/shops/{id}/"})
    public ResponseEntity<Shop> updateShop(@PathVariable long id, @RequestBody Map<String, Object> requestData) {
        String type = (String) requestData.get("type");
        int extension = Integer.parseInt((String) requestData.get("extension"));
        int workersNum = Integer.parseInt((String) requestData.get("workersNum"));
        List<String> cityNames = (List<String>) requestData.get("cities");
        List<City> cities = new ArrayList<>();
        for (String cityName : cityNames) {
            City city = citiesRepository.getCityByName(cityName);
            if (city != null) {
                cities.add(city);
            }
        }
        Shop shop = new Shop(ShopType.valueOf(type), extension, workersNum);
        for (City city : cities) {
            city.addShopType(shop.getId(), shop.getType());
            shop.addCity(city);
        }

        if (this.shoprepository.isShop(id)) {
            this.shoprepository.updateShop(shop, id);
            return new ResponseEntity(shop, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }






    @GetMapping({"/api/products/{id6}/"})
    public ResponseEntity getAllProducts(@PathVariable long id6) {
        return new ResponseEntity(this.shoprepository.returnMyProducts2(id6), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping({"/api/products/{id7}/"})
    public Product createProduct(@RequestBody Product product, @PathVariable Long id7) {
        this.shoprepository.putProductsMap(product, id7);
        return product;
    }

    @DeleteMapping({"/api/products/{id2}/{id3}/"})
    public ResponseEntity<Product> removeProductByID(@PathVariable Long id2, @PathVariable Long id3) {
        if (this.shoprepository.isProduct(id2, id3)) {
            this.shoprepository.removeProduct2(id2, id3);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping({"/api/products/{id4}/{id5}/"})
    public ResponseEntity<Shop> putProductByID(@RequestBody Product product, @PathVariable Long id4, @PathVariable Long id5) {
        if (this.shoprepository.isProduct(id4, id5)) {
            this.shoprepository.updateObject(product, id4, id5);
            return new ResponseEntity(product, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping({"/api/cities/{id7}/"})
    public Shop createShop(@RequestBody Shop shop, @PathVariable Long id7) {
        if(!this.shoprepository.containsShop(shop.getType())){
            this.shoprepository.putShopsMap(shop);
        }
        this.citiesRepository.getCitybyID(id7).putMap(shop, shop.getId());
        return shop;
    }



}

