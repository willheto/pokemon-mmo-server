package com.g8e.loginserver.models;

public class LoginResponse {
    private int type;
    private int response;
    private String loginToken;

    public LoginResponse(int type, int response) {
        this.type = type;
        this.response = response;
    }

    public LoginResponse(int type, int response, String loginToken) {
        this.type = type;
        this.response = response;
        this.loginToken = loginToken;
    }

    public int getType() {
        return type;
    }

    public int getResponse() {
        return response;
    }

    public String getLoginToken() {
        return loginToken;
    }

}
