package com.example.dws.Service;

import com.example.dws.Entities.Product;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public Optional<User> findById(long id) {
        return (userRepository.findById(id));
    }
    public void save(User user) {userRepository.save(user);}

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void addProduct(User user,Product product) {
        user.addProduct(product);
    }
    public Long getId(User user) {
        return user.getId();
    }
    public Optional<User> findByName(String userName) {
        return userRepository.findByName(userName);
    }
}

