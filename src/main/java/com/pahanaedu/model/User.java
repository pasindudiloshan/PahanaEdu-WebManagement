package com.pahanaedu.model;

import java.io.InputStream;

public class User {
    private int id;  // if you have id field, else remove this line
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String role;
    private InputStream photo;

    public User() {
        // default constructor
    }

    public User(String name, String email, String password, String mobile, String role, InputStream photo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.role = role;
        this.photo = photo;
    }

    // If you use id, add constructor with id
    public User(int id, String name, String email, String password, String mobile, String role, InputStream photo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.role = role;
        this.photo = photo;
    }

    // Getters and setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public InputStream getPhoto() {
        return photo;
    }
    public void setPhoto(InputStream photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", mobile=" + mobile + ", role=" + role + "]";
    }
}

