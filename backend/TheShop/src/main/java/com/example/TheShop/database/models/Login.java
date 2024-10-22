package com.example.TheShop.database.models;

public class Login {
    private Long id;
    private String email;
    private String password;
    private String googleId;
    private String authType;

    public Login() {}

    public Login(Long id, String email, String password, String googleId, String authType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.googleId = googleId;
        this.authType = authType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
}
