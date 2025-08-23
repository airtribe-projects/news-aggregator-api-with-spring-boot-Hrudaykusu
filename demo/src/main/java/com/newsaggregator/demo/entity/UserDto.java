package com.newsaggregator.demo.entity;

import java.util.List;

public class UserDto {

    private String username;
    private String password;
    private List<String> preferences;


    public UserDto() {
    }

    public UserDto(List<String> preferences, String password, String username) {
        this.preferences = preferences;
        this.password = password;
        this.username = username;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
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
}
