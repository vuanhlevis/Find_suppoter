package com.example.anull.findsuppoter.model;

/**
 * Created by null on 24/11/2017.
 */

public class User {
    private String email, password, name, phone, available, rating;
    private String location;


    public String getAvailable() {
        return available;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public User() {
        this.available = "0";
        this.location = "0,0";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User(String email, String password, String name, String phone, String rating, String location) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.location = location;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}