package com.example.dws.Entities;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;
    @ManyToOne
    private User user;
    private String issue;
    private String message;



    public Comment() {

    }

    public Comment(User user, String issue, String message) {
        this.user = user;
        this.issue = issue;
        this.message = message;
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
