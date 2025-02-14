package com.example.dws.Repositories;

import com.example.dws.Entities.Comment;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CommentRepository {
    private Map<Long, Comment> comments = new ConcurrentHashMap();
    private AtomicLong commentID = new AtomicLong();

    public CommentRepository() {
    }

    public CommentRepository(Map<Long, Comment> comments, AtomicLong commentID) {
        this.comments = comments;
        this.commentID = commentID;
    }

    public Comment putComment(Comment comment) {
        long id = this.commentID.incrementAndGet();
        comment.setId(id);
        this.comments.put(id, comment);
        return comment;
    }

    public Collection<Comment> showComments() {
        return this.comments.values();
    }
}
