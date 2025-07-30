package com.pahanaedu.model;

import java.io.InputStream;

/**
 * Model class representing a User entity.
 */
public class User {
    private int id;                  // Auto-incremented ID
    private String uempid;           // Unique employee ID
    private String name;             // Full name
    private String email;            // Email (unique)
    private String password;         // Password (should be hashed)
    private String mobile;           // Mobile number
    private String role;             // Role (admin, employee, etc.)
    private InputStream photo;       // Photo as InputStream

    public User() {
        // Default constructor
    }

    // Constructor without ID
    public User(String uempid, String name, String email, String password, String mobile, String role, InputStream photo) {
        this.uempid = uempid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.role = role;
        this.photo = photo;
    }

    // Full constructor with ID
    public User(int id, String uempid, String name, String email, String password, String mobile, String role, InputStream photo) {
        this.id = id;
        this.uempid = uempid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.role = role;
        this.photo = photo;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUempid() {
        return uempid;
    }

    public void setUempid(String uempid) {
        this.uempid = uempid;
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
        return "User [id=" + id + ", uempid=" + uempid + ", name=" + name + ", email=" + email +
               ", mobile=" + mobile + ", role=" + role + "]";
    }
}
