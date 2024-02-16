package com.recruitment.githubapi.model;

import java.util.List;

/**
 * Represents detailed information about a GitHub repository.
 * This class encapsulates the repository name, owner's login, and list of branches.
 * This class does not encapsulate the repository fork status.
 */
public class RepositoryDetails {
    private String repoName; // The name of the repository
    private String ownerLogin; // The login username of the repository owner
    private List<String> branches; // List of branches in the repository

    // Getters and setters
    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }


}
