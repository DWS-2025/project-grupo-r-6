package com.example.dws.Repositories;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import com.example.dws.Entities.ShopType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class ShopRepository {
    private Map<Long, Shop> shopsMap = new ConcurrentHashMap();
    private AtomicLong lastID = new AtomicLong();
    private int shopCounter = 0;
    private long productLastId = 1;





    public ShopRepository() {
    }

    public ShopRepository(Map<Long, Shop> shopsMap, AtomicLong lastID, int shopCounter, long productLastId) {
        this.shopsMap = shopsMap;
        this.lastID = lastID;
        this.shopCounter = shopCounter;
        this.productLastId = productLastId;
    }


    public Shop putShopsMap(Shop shop) {
        long id = this.lastID.incrementAndGet();
        shop.setId(id);
        this.shopsMap.put(id, shop);
        ++this.shopCounter;
        return shop;
    }

    public void putShopsMap2(Shop shop) {
        long id = this.lastID.incrementAndGet();
        shop.setId(id);
        this.shopsMap.put(id, shop);
        ++this.shopCounter;
    }

    public List<Shop> getAllShops(){
        return new ArrayList<>(shopsMap.values());
    }

    public boolean containsShop(ShopType type){
        for(Shop s: this.shopsMap.values()){
            if (s.getType().equals(type)){
                return true;
            }
        }
        return false;
    }
    public Shop getShopById(Long id) {
        return shopsMap.get(id);
    }



        public Product putProductsMap(Product product, Long ID) {
        ((Shop)this.shopsMap.get(ID)).putMap(product,this.productLastId);
        this.productLastId++;
        return product;
    }
    public Collection<Shop> returnMyShops() {
        if(this.shopsMap.values() != null){
            return this.shopsMap.values();
        } else{
            return null;
        }

    }
    public ResponseEntity<Shop> removeShop(Long id) {
        Shop shop = this.shopsMap.remove(id);
        return shop != null ? new ResponseEntity(shop, HttpStatus.OK) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    public Collection<Product> returnMyProducts2(Long id){

        return  this.shopsMap.get(id).showProducts();
    }
    public boolean isProduct(Long id, Long ID) {
        return this.shopsMap.get(id).isProduct(ID);
    }



    public ResponseEntity<Shop> updateShop(Shop shop, long ID) {
        Shop tempShop = (Shop)this.shopsMap.get(ID);
        if (tempShop != null) {
            shop.setId(ID);
            this.shopsMap.put(ID, shop);
            return new ResponseEntity(shop, HttpStatus.OK);
        } else {
            return new ResponseEntity(shop, HttpStatus.NOT_FOUND);
        }
    }

    public Map<Long, Shop> showShops() {
        return this.shopsMap;
    }
    public void setShopCounter(int num) {
        this.shopCounter = num;
    }
    public boolean isShop(Long id) {
        return this.shopsMap.get(id) != null;
    }
    public void removeShopProducts(Long id){
        this.shopsMap.get(id).removeAllProducts();
    }
    public int getShopCounter() {
        return this.shopCounter;
    }

    public ResponseEntity<Product> removeProduct(Long id, Long ID) {
        Product product = this.shopsMap.get(ID).removeMap(id);
        return product != null ? new ResponseEntity(product, HttpStatus.OK) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    public ResponseEntity<Product> removeProduct2(Long ID, Long id) {
        Product product = this.shopsMap.get(ID).removeMap(id);
        return product != null ? new ResponseEntity(product, HttpStatus.OK) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    public Collection<Product> showProductsShop(Long ID) {
        return ((Shop)this.shopsMap.get(ID)).showProducts();
    }
    public Product showProduct(Long shopId, Long objectId){
        return this.shopsMap.get(shopId).showProduct(objectId);
    }
    public ResponseEntity<Shop> updateObject(Product product, long ShopId,long ProductId) {
        Product temproduct =this.shopsMap.get(ShopId).showProduct(ProductId);

        if (temproduct != null) {
            temproduct.setId(ProductId);
            this.shopsMap.get(ShopId).putMap(product,ProductId);
            return new ResponseEntity(product, HttpStatus.OK);
        } else {
            return new ResponseEntity(product, HttpStatus.NOT_FOUND);
        }
    }

}
