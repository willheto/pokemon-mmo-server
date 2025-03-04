package com.g8e.loginserver.models;

public class Account {
    private int accountID;
    private String username;
    private String password;
    private String loginToken;
    private String registrationIp;
    private String registrationDate;

    public Account(int accountId, String username, String password, String loginToken, String registrationIp,
            String registrationDate) {
        this.accountID = accountId;
        this.username = username;
        this.password = password;
        this.loginToken = loginToken;
        this.registrationIp = registrationIp;
        this.registrationDate = registrationDate;
    }

    public Account() {
        // TODO Auto-generated constructor stub
    }

    public int getAccountId() {
        return accountID;
    }

    public int setAccountID(int accountID) {
        return this.accountID = accountID;
    }

    public String getUsername() {
        return username;
    }

    public String setUsername(String username) {
        return this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String setPassword(String password) {
        return this.password = password;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public String getRegistrationIp() {
        return registrationIp;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }
}
