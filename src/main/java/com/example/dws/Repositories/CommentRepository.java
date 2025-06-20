package com.example.dws.Repositories;

import com.example.dws.Entities.Comment;
import com.example.dws.Entities.Shop;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByShop_ShopID(Long shopID, Pageable pageable);
    @Transactional
    void deleteByShop_ShopID(Long shopID);

    void deleteByUserId(long userID);



    /*
    private HashMap<Long, Comment> coments = new HashMap<>();   // Use Long as the key in the HashMap
    private AtomicLong nextId = new AtomicLong(0); // To generate unique IDs for the shops

    public Collection<Comment> findAll() {
        return coments.values();
    }

    // Save or update a comment
    public Comment findById(long id) {
        return coments.get(id);
    }

    // Delete a comment by its ID
    public void save(Comment comment) {
        this.coments.put(comment.getCommentId(), comment);  // Save or update the comment in the map
    }

    // Delete a comment by its ID
    public void deleteById(long id) {
        this.coments.remove(id);  // Remove comment
    }

     */
}
