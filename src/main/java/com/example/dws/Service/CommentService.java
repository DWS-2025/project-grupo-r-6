package com.example.dws.Service;
import java.util.List;
import java.util.Optional;

import com.example.dws.DTOs.*;
import com.example.dws.Repositories.ShopRepository;
import com.example.dws.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Shop;
import com.example.dws.Repositories.CommentRepository;
import com.example.dws.DTOs.GeneralMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private GeneralMapper generalMapper;

    public List<CommentDTO> findAll(){
        return generalMapper.ToListCommentDTO(commentRepository.findAll());
    }

    public Optional<CommentDTO> findById(long id) {
        CommentDTO commentDTO= commentToCommentDTO(commentRepository.findById(id));
        return Optional.of(commentDTO);
    }
    public Optional<CommentBasicDTO> findCommentById(long id) {
        CommentBasicDTO commentDTO= commentToCommentBasicDTO(commentRepository.findById(id));
        return Optional.of(commentDTO);
    }


    public void save(CommentDTO commentDTO) {
        commentRepository.save(commentDTOToComment(commentDTO));
    }
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
    public void delete(long commentID, long shopID) {
        Optional<Comment> comment = commentRepository.findById(commentID);
        Optional<Shop> shop = shopRepository.findById(shopID);
        shop.get().getComments().remove(comment.get());
        shopRepository.save(shop.get());
        commentRepository.deleteById(commentID);
    }

    private CommentDTO  productToProductDTO(Optional<Comment> comment) {
        return generalMapper.commentToCommentDTO(comment.get());
    }
    private Comment commentDTOToComment(CommentDTO commentDTO) {
        return generalMapper.commentDTOToComment(commentDTO);
    }
    private CommentDTO commentToCommentDTO(Optional<Comment> comment){
        return generalMapper.commentToCommentDTO(comment.get());
    }
    private Comment commentBasicDTOToComment(CommentBasicDTO commentDTO) {
        return generalMapper.commentBasicDTOToComment(commentDTO);
    }
    private CommentBasicDTO commentToCommentBasicDTO(Optional<Comment> comment){
        return generalMapper.commentToCommentBasicDTO(comment.get());
    }

    public Page<CommentDTO> paginateShopComments(ShopDTO shopDTO, Pageable pageable) {
        List<CommentDTO> allComments = shopDTO.comments(); // Lista completa

        int total = allComments.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        List<CommentDTO> paginatedList = allComments.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, total);
    }
    public Page<Comment> findByShopIdPaginated(Long shopId, Pageable pageable) {
        return commentRepository.findByShop_ShopID(shopId, pageable);
    }


    public Page<Comment> findAllPaginated(Pageable pageable){
        return commentRepository.findAll(pageable);
    }

    private Shop shopDTOToShop(ShopDTO shopDTO) {
        return generalMapper.shopDTOToShop(shopDTO);
    }

    @Transactional
    public void deleteByUserId(long userID) {
        commentRepository.deleteByUserId(userID);
    }
}
