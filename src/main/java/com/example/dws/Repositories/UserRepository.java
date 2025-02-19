package com.example.dws.Repositories;

import com.example.dws.Entities.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private HashMap<Long, User> users = new HashMap<>();
    private AtomicLong nextId = new AtomicLong(0);

    public void save(User user){
        this.users.put(user.getId(), user);
    }

    public User findByID(long userID){
        return users.get(userID);
    }

    public User findByName(String userName){
        for(User user : users.values()){
            if(user.getName().equals(userName)){
                return user;
            }
        }
        return null;
    }

    public void deleteByID(long userID){
        this.users.remove(userID);
    }

    public Collection<User> findAll(){
        return users.values();
    }
}
