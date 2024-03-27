package com.example.assetsystembackend.api.payload.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents a request payload for user login, containing the username and password
 */
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;


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
}
