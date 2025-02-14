package com.example.dws.Repositories;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Enums.ShopType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class ShopRepository {
    private HashMap<Long, Shop> shops = new HashMap<>();  // HashMap para almacenar las tiendas
    private AtomicLong nextId = new AtomicLong(1);  // Generación de ID para las tiendas

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
        if (shop.getShopID() == 0) {
            long id = nextId.getAndIncrement();
            shop.setShopID(id);  // Asignar un nuevo ID
        }
        this.shops.put(shop.getShopID(), shop);  // Guardar o actualizar la tienda en el mapa
    }

    // Eliminar una tienda por su ID
    public void deleteById(long id) {
        this.shops.remove(id);  // Eliminar la tienda
    }

    // Agregar un producto a una tienda específica
    public void addProductToShop(long shopId, Product product) {
        Shop shop = shops.get(shopId);
        if (shop != null) {
            shop.getProducts().put(product.getId(), product);  // Agregar el producto al HashMap de productos de la tienda
        }
    }

    // Eliminar un producto de una tienda específica
    public void removeProductFromShop(long shopId, Product product) {
        Shop shop = shops.get(shopId);
        if (shop != null) {
            shop.removeProduct(product);  // Llamar al método removeProduct de Shop
        }
    }
}
