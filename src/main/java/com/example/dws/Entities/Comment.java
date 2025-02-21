package com.example.dws.Entities;

import java.util.concurrent.atomic.AtomicLong;

public class Comment {
    private User user;
    private String issue;
    private String message;
    private long id;
    private static AtomicLong counter= new AtomicLong(0);


    public Comment(User user, String issue, String message) {
        this.user = user;
        this.issue = issue;
        this.message = message;
        this.id = counter.getAndIncrement();
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
        return this.id;
    }

    public void setCommentId(long id) {
        this.id = id;
    }
}
