package com.example.dws.Entities;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long commentId;
    @OneToOne
    private User user;
    private String issue;
    private String message;


    @ManyToOne
    @JoinColumn(name = "shop_shopid")
    private Shop shop;

    public Comment() {

    }

    public Comment(User user, String issue, String message, Shop shop) {
        this.user = user;
        this.issue = issue;
        this.message = message;
        this.shop = shop;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIssue() {
        return this.issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getMessage() {
        return this.message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public long getCommentId() {
        return this.commentId;
    }

    public void setCommentId(long id) {
        this.commentId = id;
    }

}
