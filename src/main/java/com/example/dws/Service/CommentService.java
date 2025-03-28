package com.example.dws.Service;
import java.util.Optional;

import com.example.dws.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.CommentRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    public void save(Shop shop, Comment comment) {
        shop.getComments().add(comment);
        commentRepository.save(comment);
    }

    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
    public void delete(Long commentId, Shop shop) {
        Comment comment = this.findById(commentId).get();
        shop.getComments().remove(comment);
        commentRepository.delete(comment);
    }
}
