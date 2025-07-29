package com.pahanaedu.model;

import java.io.InputStream;

/**
 * Model class representing a User entity.
 */
public class User {
    private int id;                // User ID, optional if your DB uses auto-increment
    private String name;           // User's full name
    private String email;          // User email (unique)
    private String password;       // User password (hashed ideally)
    private String mobile;         // User mobile number
    private String role;           // User role (e.g., admin, employee)
    private InputStream photo;     // User photo (as InputStream)

    public User() {
        // Default constructor
    }

    public User(String name, String email, String password, String mobile, String role, InputStream photo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.role = role;
        this.photo = photo;
    }

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
