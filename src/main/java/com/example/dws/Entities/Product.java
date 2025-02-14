package com.example.dws.Entities;


import java.util.HashMap;

public class Product {
    private Type type;
    private Size size;
    private String brand;
    private double prize;
    private long id = 0;
    private HashMap<Long, Shop> shops;

    public Product(Type type, Size size, double prize, String brand) {
        this.type = type;
        this.size = size;
        this.prize = prize;
        this.brand = brand;
        this.shops = new HashMap();
    }

    public Shop putMap(Shop shop, Long id) {
        shop.setId(id);
        this.shops.put(id, shop);
        return shop;
    }

    public void putShop(Shop shop, Long id){
        this.shops.put(id, shop);
    }

    public Shop removeMap(Long id) {
        Shop shop =this.shops.remove(id);
        return shop != null ? shop : null;
    }

    public void removeAllShopsMap(){
        shops.clear();
    }



    public Type gettype() {
        return this.type;
    }

    public Size getsize() {
        return this.size;
    }

    public double getprize() {
        return this.prize;
    }

    public String getbrand() {
        return this.brand;
    }

    public long getId() {
        return this.id;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public void setId(long id) {
        this.id = id;
    }
}
