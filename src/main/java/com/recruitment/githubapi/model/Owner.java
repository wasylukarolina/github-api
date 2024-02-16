package com.recruitment.githubapi.model;

/**
 * Represents the owner of a GitHub repository.
 * It contains information about the username of the owner.
 */

public class Owner {
    private String login; // The username of the owner of the repository

    // Getters and setters
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}