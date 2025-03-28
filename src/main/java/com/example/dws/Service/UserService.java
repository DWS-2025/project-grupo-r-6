package com.example.dws.Service;

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


    public User findUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }


    public User getUserByName(String userName) {
        return userRepository.findByName(userName);
    }
}

