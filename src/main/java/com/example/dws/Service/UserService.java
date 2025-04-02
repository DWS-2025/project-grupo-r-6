package com.example.dws.Service;

import com.example.dws.DTOs.GeneralMapper;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.UserDTO;
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

    @Autowired
    private GeneralMapper generalMapper;


    public Optional<UserDTO> findById(long id) {
        return (Optional.of(userToUserDTO(userRepository.findById(id))));
    }
    public void save(UserDTO userDTO) {
        User user = userDTOToUser(userDTO);
        userRepository.save(user);
    }

    public void save(User user){
        userRepository.save(user);
    }

    public List<UserDTO> findAll() {
        return ToListUserID(userRepository.findAll());
    }

    public void addProduct(UserDTO userDTO,ProductDTO productDTO) {
        User user = userDTOToUser(userDTO);
        Product product = generalMapper.productDTOToProduct(productDTO);
        user.addProduct(product);
    }
    public Long getId(UserDTO userDTO) {
        return userDTOToUser(userDTO).getId();
    }
    public Optional<UserDTO> findByName(String userName) {
        return Optional.of(userToUserDTO(userRepository.findByName(userName)));
    }
    private UserDTO userToUserDTO(Optional<User> user) {
        return generalMapper.userToUserDTO(user.get());
    }
    private User userDTOToUser(UserDTO userDTO){
        return generalMapper.userDTOToUser(userDTO);
    }

    private List<UserDTO> ToListUserID(List<User> users){
        return generalMapper.ToListUserID(users);
    }
}

