package com.ats.edetailingapp.model;

public class UserDataModel {

    private int userId;
    private String username;
    private String password;
    private int tId1;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int gettId1() {
        return tId1;
    }

    public void settId1(int tId1) {
        this.tId1 = tId1;
    }

    @Override
    public String toString() {
        return "UserDataModel{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", tId1=" + tId1 +
                '}';
    }
}
