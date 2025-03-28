package com.example.dws.Repositories;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    /*
    private HashMap<Long, Shop> shops = new HashMap<>();  // Use Long as the key in the HashMap
    private AtomicLong nextId = new AtomicLong(0); // To generate unique IDs for the shops



    // Get all the shops
    public Collection<Shop> findAll() {
        return shops.values();
    }

    // Find a shop by its ID
    public Shop findById(long id) {
        return shops.get(id);
    }

    // Save or update a shop
    public void save(Shop shop) {
        this.shops.put(shop.getShopID(), shop);  //Save or update the shop in the map
    }

    // Delete a shop by its ID
    public void deleteById(long id) {
        this.shops.remove(id);  // Eliminar la tienda
    }

    // Delete the products from all shops
    public void removeProductFromAllShops(long productId) {
        for (Shop shop : shops.values()) {
            shop.getProducts().remove(productId);
        }
    }

     */

}
