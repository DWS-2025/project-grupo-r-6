package com.example.dws.Controllers;

import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Collection;

@Controller
public class ShopController {
    @Autowired
    private ShopRepository shopRepository;

    // Mostrar todas las tiendas
    @GetMapping("/index")
    public String getAllShops(Model model) {
        Collection<Shop> shops = shopRepository.findAll();
        model.addAttribute("shops", shops);
        return "index"; // Vista que muestra todas las tiendas
    }

    // Ver detalles de una tienda por su ID
    @GetMapping("/{shopId}")
    public String getShopById(@PathVariable long shopId, Model model) {
        Shop shop = shopRepository.findById(shopId);
        if (shop != null) {
            model.addAttribute("shop", shop);
            return "shopDetails"; // Vista que muestra los detalles de la tienda
        } else {
            return "error"; // Vista de error si no se encuentra la tienda
        }
    }

    // Crear una nueva tienda (Formulario)
    @GetMapping("/new")
    public String createShopForm(Model model) {
        model.addAttribute("shop", new Shop("")); // Inicializar un objeto Shop vacío
        return "shopForm"; // Vista con el formulario para crear una tienda
    }

    // Guardar una nueva tienda
    @PostMapping("/save")
    public String saveShop(@ModelAttribute Shop shop) {
        shopRepository.save(shop); // Guardamos la tienda en el repositorio
        return "redirect:/shops"; // Redirige a la lista de tiendas
    }

    // Eliminar una tienda
    @PostMapping("/delete")
    public String deleteShop(@RequestParam long shopID) {
        shopRepository.deleteById(shopID); // Eliminar la tienda por su ID
        return "redirect:/shops"; // Redirige a la lista de tiendas
    }
}
