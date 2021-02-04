package com.masmovil.userModule.dto;

public class AuthenticationResponse {

    private final String jwt;
    private String message;
    private User user;

    public AuthenticationResponse(String jwt, String message, User user) {
        this.jwt = jwt;
        this.message = message;
        this.user = user;
    }


    public String getJwt() {
        return jwt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
