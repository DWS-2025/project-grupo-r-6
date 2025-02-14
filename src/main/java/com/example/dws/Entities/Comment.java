package com.example.dws.Entities;

public class Comment {
    private String name;
    private String mail;
    private String issue;
    private String message;
    private long id;

    public Comment(String name, String mail, String issue, String message) {
        this.name = name;
        this.mail = mail;
        this.issue = issue;
        this.message = message;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
