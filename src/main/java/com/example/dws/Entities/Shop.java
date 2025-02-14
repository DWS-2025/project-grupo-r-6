package com.example.dws.Entities;

import com.example.dws.Enums.ShopType;

import java.util.*;

public class Shop {
    private ShopType type;
    private int extension;
    private int workersNum;
    private long id = 0;
    private HashMap<Long, Product> products;
    private HashMap<Long, City> cities;
    private List<City> listacities;


    public Shop() {
    }

    public Shop(ShopType type, int extension, int workersNum) {
        this.type = type;
        this.extension = extension;
        this.workersNum = workersNum;
        this.products = new HashMap();
        this.cities = new HashMap<>();
        this.listacities = new ArrayList<>();
    }

    public Shop(ShopType type, int extension, int workersNum, String city2, String country){
        this.type = type;
        this.extension = extension;
        this.workersNum = workersNum;
        this.products = new HashMap();
        this.cities = new HashMap<>();
        this.listacities = new ArrayList<>();
        City city = new City(city2, country);
        this.cities.put(city.getId(), city);
        this.listacities.add(city);
    }

    public void addCity(City city){
        this.cities.put(city.getId(), city);
        this.listacities.add(city);
    }

    public void deleteCity(String name) {
        for (Iterator<Map.Entry<Long, City>> iterator = cities.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<Long, City> entry = iterator.next();
            City city = entry.getValue();
            if (city.getName().equals(name)) {
                iterator.remove();
            }
        }
    }




    public ShopType getType() {
        return this.type;
    }

    public int getExtension() {
        return this.extension;
    }

    public int getworkersNum() {
        return this.workersNum;
    }

    public void setType(ShopType type) {
        this.type = type;
    }

    public void setExtension(int extension) {
        this.extension = extension;
    }

    public void setworkersNum(int workersNum) {
        this.workersNum = workersNum;
    }

    public long getId() {
        return this.id;
    }


    public String toString() {
        return this.type + "\n";
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product putMap(Product producto, Long id) {
        producto.setId(id);
        this.products.put(id, producto);
        return producto;
    }

    public Product removeMap(Long id) {
        Product product =this.products.remove(id);
        return product != null ? product : null;
    }

    public Collection<Product> showProducts() {
        return this.products.values();
    }

    public Product showProduct(Long productId){
        return this.products.get(productId);
    }

    public boolean isProduct(Long id){
        return this.products.get(id) != null;
    }

    public void removeAllProducts(){
        this.products.clear();
    }


}
