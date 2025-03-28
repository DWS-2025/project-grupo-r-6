package com.example.dws.Repositories;

import com.example.dws.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByName(String userName);
    /*
    private HashMap<Long, User> users = new HashMap<>();
    private AtomicLong nextId = new AtomicLong(0);

    public void save(User user){
        this.users.put(user.getId(), user);
    }

    public User findByID(long userID){
        return users.get(userID);
    }



    public void deleteByID(long userID){
        this.users.remove(userID);
    }

    public Collection<User> findAll(){
        return users.values();
    }

     */
}
