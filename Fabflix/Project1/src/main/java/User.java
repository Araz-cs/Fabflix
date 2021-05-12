package main.java;

public class User {

    private final String username;
    private String role;

    public User(String username) {
        this.username = username;
    }
    public String getRole(){ return role; }
}