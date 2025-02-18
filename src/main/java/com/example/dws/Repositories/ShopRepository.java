package com.example.dws.Repositories;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ShopRepository {
    private HashMap<Long, Shop> shops = new HashMap<>();  // Usamos Long como clave en el HashMap
    private AtomicLong nextId = new AtomicLong(1); // Para generar IDs únicos para las tiendas



    // Obtener todas las tiendas
    public Collection<Shop> findAll() {
        return shops.values();
    }

    // Encontrar una tienda por su ID
    public Shop findById(long id) {
        return shops.get(id);
    }

    // Guardar o actualizar una tienda
    public void save(Shop shop) {
        this.shops.put(shop.getShopID(), shop);  // Guardar o actualizar la tienda en el mapa
    }

    // Eliminar una tienda por su ID
    public void deleteById(long id) {
        this.shops.remove(id);  // Eliminar la tienda
    }

    // Agregar un producto a una tienda específica
    public void addProductToShop(long shopId, Product product) {
        Shop shop = shops.get(shopId);  // Obtener la tienda por su ID
        if (shop != null) {
            shop.getProducts().put(product.getProductId(), product);  // Agregar el producto
        }
    }

    // Eliminar un producto de una tienda específica
    public void removeProductFromShop(long shopId, Product product) {
        Shop shop = shops.get(shopId);  // Obtener la tienda por su ID
        if (shop != null) {
            shop.removeProduct(product);  // Llamar al método removeProduct de Shop
        }
    }
}
