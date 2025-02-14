package com.example.dws.Repositories;

import com.example.dws.Entities.City;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.Shop;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Repository

public class CitiesRepository {
    private Map<Long, City> cities = new ConcurrentHashMap();
    private AtomicLong cityID = new AtomicLong();

    public CitiesRepository() {
    }

    public CitiesRepository(Map<Long, City> cities, AtomicLong cityID) {
        this.cities = cities;
        this.cityID = cityID;
    }
    public City putCity(City city) {
        long id = this.cityID.incrementAndGet();
        city.setId(id);
        this.cities.put(id, city);
        return city;
    }
    public Collection<City> returnMyCities() {
        if(this.cities.values() != null){
            return this.cities.values();
        } else{
            return null;
        }

    }

    public City getCityByName(String cityName) {
        for (Map.Entry<Long, City> entry : this.cities.entrySet()) {
            City city = entry.getValue();
            if (city.getName().equals(cityName)) {
                return city;
            }
        }
        return null;
    }

    public City findById(Long id){
        return this.cities.get(id);
    }

    public ResponseEntity<City> removeCity(Long id) {
        City city = this.cities.get(id);
        if(city.containsShops()){
            city.deleteShops();
        }
        this.cities.remove(id);
        return city != null ? new ResponseEntity(city, HttpStatus.OK) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public City getCitybyID(Long id){
        return this.cities.get(id);
    }
    public boolean isCity(Long id) {
        return this.cities.get(id) != null;
    }




}
