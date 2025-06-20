package com.example.dws.Service;

import com.example.dws.DTOs.GeneralMapper;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.UserDTO;
import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Product;
import com.example.dws.Entities.User;
import com.example.dws.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralMapper generalMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Optional<UserDTO> findById(long id) {
        return (Optional.of(userToUserDTO(userRepository.findById(id))));
    }


    public User getLoggedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUserName(username).get();
    }

    public UserDTO getLoggedUserDTO() {
        return generalMapper.userToUserDTO(getLoggedUser());
    }

    public void save(User user){
        userRepository.save(user);
    }

    public List<UserDTO> findAll() {
        return ToListUserID(userRepository.findAll());
    }

    public Long addProduct(ProductDTO productDTO) {
        User user = this.getLoggedUser();
        Product product = generalMapper.productDTOToProduct(productDTO);
        if(!user.getUserProducts().contains(product)){
            user.addProduct(product);
            this.save(user);
        }
        return user.getId();
    }

    public void removeProductFromUser(ProductDTO productDTO){
        User user = this.getLoggedUser();
        Product toRemove = null;

        for (Product p : user.getUserProducts()) {
            if (p.getProductId() == productDTO.productId()) {
                toRemove = p;
                break;
            }
        }

        if (toRemove != null) {
            user.getUserProducts().remove(toRemove);
            userRepository.save(user);
        }
    }

    public Long getId(UserDTO userDTO) {
        return userDTOToUser(userDTO).getId();
    }

    public Optional<UserDTO> findByName(String userName) {
        Optional<User> user = userRepository.findByUserName(userName);
        if(user.isPresent()){
            return Optional.of(generalMapper.userToUserDTO(user.get()));
        }else{
            return Optional.empty();
        }
    }
    public UserDTO getUser(String name) {
        return generalMapper.userToUserDTO(userRepository.findByUserName(name).orElseThrow());
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

    public void save(UserDTO userDTO){
        User user = userDTOToUser(userDTO);
        userRepository.save(user);

    }

    public void deleteById(long userID) {
        userRepository.deleteById(userID);
    }

    public void update(Long id, UserDTO userDTO) {
        Optional<User> oldUser = userRepository.findById(id);
        User newUser = userDTOToUser(userDTO);
        oldUser.get().setUserName(newUser.getUserName());
        oldUser.get().setEmail(newUser.getEmail());
        userRepository.save(oldUser.get());
    }

    public void updatePassword(long userID, String password) {
        User user = userRepository.findById(userID).orElseThrow();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}

