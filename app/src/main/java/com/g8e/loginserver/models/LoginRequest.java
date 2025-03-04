package com.g8e.loginserver.models;

public class LoginRequest {

    private int type;
    private int world;
    private String username;
    private String password;

    public int getType() {
        return type;
    }

    public int getWorld() {
        return world;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
