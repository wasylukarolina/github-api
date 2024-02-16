package com.recruitment.githubapi.model;

/**
 * Represents a commit in a GitHub repository.
 * It contains information about the SHA code of the commit.
 */

public class Commit {
    private String sha; // The SHA of the commit in the branch

    // Getters and setters
    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }
}
