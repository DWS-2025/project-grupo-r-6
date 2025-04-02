package com.example.dws.Service;
import java.util.Optional;

import com.example.dws.DTOs.CommentDTO;
import com.example.dws.DTOs.GeneralMapper;
import com.example.dws.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.CommentRepository;
import com.example.dws.DTOs.GeneralMapper;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private GeneralMapper generalMapper;

    public Optional<CommentDTO> findById(long id) {
        CommentDTO commentDTO= commentToCommentDTO(commentRepository.findById(id));
        return Optional.of(commentDTO);
    }

    public void save(CommentDTO commentDTO) {
        commentRepository.save(commentDTOToComment(commentDTO));
    }
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
    /*public void delete(Long commentId, Shop shop) {
        Comment comment = this.findById(commentId).get();
        shop.getComments().remove(comment);
        commentRepository.delete(comment);
    }*/

    private CommentDTO  productToProductDTO(Optional<Comment> comment) {
        return generalMapper.commentToCommentDTO(comment.get());
    }
    private Comment commentDTOToComment(CommentDTO commentDTO) {
        return generalMapper.commentDTOToComment(commentDTO);
    }
    private CommentDTO commentToCommentDTO(Optional<Comment> comment){
        return generalMapper.commentToCommentDTO(comment.get());
    }
}
