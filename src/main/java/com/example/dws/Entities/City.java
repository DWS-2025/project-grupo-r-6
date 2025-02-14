package com.example.dws.Entities;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;

public class City {

    private String name;
    private HashMap<Long, Shop> shops;
    private String country;
    private long id;
    private String imageFileName;

    public City() {
    }

    public City(HashMap<Long, Shop> shops, String name, String country, long id) {
        this.shops = shops;
        this.name = name;
        this.country = country;
        this.id = id;
    }

    public City(String name, String country) {
        this.name = name;
        this.country = country;
        this.shops = new HashMap<>();
    }

    public City(String name, String country, String fileName) {
        this.name = name;
        this.country = country;
        this.imageFileName = fileName;
        this.shops = new HashMap<>();
    }

    public Shop putMap(Shop shop, Long id) {
        shop.setId(id);
        this.shops.put(id, shop);
        return shop;
    }

    public void addShopType(Long shopId, ShopType shopType) {
        Shop shop = shops.get(shopId);
        if (shop != null) {
            shop.setType(shopType);
        }
    }

    public boolean containsShops(){
        return !this.shops.isEmpty();
    }

    public void deleteShops(){
        for(Shop s: this.shops.values()){
            s.deleteCity(this.name);
        }
    }

    public boolean nameIs(String name){
        return this.name.equals(name);
    }

    public long getId() {
        return id;
    }

    public Collection<Shop> showShops() {
        return this.shops.values();
    }

    public HashMap<Long, Shop> getShops() {
        return shops;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setShops(HashMap<Long, Shop> shops) {
        this.shops = shops;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
