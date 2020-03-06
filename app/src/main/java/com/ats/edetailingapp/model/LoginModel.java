package com.ats.edetailingapp.model;

public class LoginModel {

    private Integer error;
    private int userid;
    private String message;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                "error=" + error +
                ", userid=" + userid +
                ", message='" + message + '\'' +
                '}';
    }
}

