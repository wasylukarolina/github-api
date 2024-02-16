package com.recruitment.githubapi.model;

/**
 * Represents a branch in a GitHub repository.
 * It contains information about the branch name and the SHA of the last commit on that branch.
 */
public class Branch{
    private String name; // The name of the branch
    private String lastCommitSha; // The SHA of the last commit in the branch

    //Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastCommitSha() {
        return lastCommitSha;
    }

    public void setLastCommitSha(String lastCommitSha) {
        this.lastCommitSha = lastCommitSha;
    }
}
