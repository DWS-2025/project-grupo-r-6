package com.example.dws.Service;
import java.util.Optional;

import com.example.dws.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.CommentRepository;

public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    public void save(Shop postToComment, Comment comment) {
        postToComment.getComments().add(comment);
        comment.setAuthor(userService.getLoggedUser());
        commentsRepository.save(comment);
    }

    public void delete(Long commentId, Post post) {
        // We assume that the comment exists
        Comment comment = this.findById(commentId).get();
        post.getComments().remove(comment);
        commentsRepository.delete(comment);
    }
}
