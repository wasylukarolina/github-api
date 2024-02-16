package com.recruitment.githubapi.model;


import com.recruitment.githubapi.model.Owner;

import java.util.List;

/**
 * Represents a GitHub repository.
 * This class encapsulates information about the repository, including its name, owner's login, fork status, owner details, and branches.
 */
public class Repository {
    private String name; // The name of the repository
    private String ownerLogin; // The login username of the repository owner
    private boolean fork; // Indicates whether the repository is a fork or not
    private Owner owner; // Details of the repository owner
    private List<String> branches; // List of branches in the repository

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }


    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }

}