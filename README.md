# GitHub API Integration

This application provides an integration with the GitHub API to retrieve information about user repositories.

## Table of Contents
1. [Introduction](#introduction)
2. [Setup](#setup)
3. [Usage](#usage)
4. [Error Handling](#errors)
4. [Endpoints](#endpoints)
5. [Models](#models)
6. [Dependencies](#dependencies)

## Introduction
This Spring Boot-based application offers a way to retrieve essential details about GitHub repositories (which are not a fork) belonging to a specific user. 
By leveraging the GitHub API, this application provides crucial information, including:

- **Repository Name:** The name of the repository.
- **Owner Login:** The username of the repository owner.
- **Branch Information:** Details about each branch, including its name and the SHA of the last commit.

## Setup
To run the application locally, follow these steps:
1. Clone the repository.
2. Ensure you have Java and Maven installed on your system.
3. Provide your GitHub personal access token in the `GitHubController.java` file:
    - Open the `GitHubController.java` file located in the `src/main/java/com/recruitment/githubapi/controller` directory.
    - Find the line `private static final String GITHUB_TOKEN = ""; //Enter your token here`.
    - Enter your GitHub personal access token between the double quotes.
    - Save the file.
4. Uncomment the line `headers.set("Authorization", "token " + GITHUB_TOKEN);` in the `GitHubController.java` file:
    - Open the `GitHubController.java` file located in the `src/main/java/com/recruitment/githubapi/controller` directory.
    - Find the line `headers.set("Authorization", "token " + GITHUB_TOKEN);`.
    - Remove the double forward slashes (`//`) at the beginning of the line to uncomment it.
    - Save the file.
5. Build the project using Maven: `mvn clean install`.
6. Run the application: `mvn spring-boot:run`.

Please note that providing the GitHub token is optional, but it may be required depending on the rate limits of the GitHub API. If you do not have a token or do not wish to provide one, you may skip steps 3 and 4.

To generate a GitHub personal access token, follow these steps:
1. Log in to your GitHub account.
2. Go to Settings > Developer settings > Personal access tokens.
3. Click on "Generate new token".
4. Enter a token description and select the scopes (permissions) you want to grant to the token.
5. Click on "Generate token".
6. Copy the generated token and paste it into the `application.properties` file.
7. Save the file.

## Usage
Once the application is up and running, you can access the endpoints to retrieve repository information. Make HTTP GET requests to the appropriate endpoints with the username of the GitHub user whose repositories you want to fetch.


## Error Handling

The application handles 404 error to provide informative responses in case of failures.

### 404 Not Found

If an incorrect username is entered or the specified user does not exist, the application returns a 404 Not Found error. This is handled by catching the `HttpClientErrorException.NotFound` exception in the code. The application then responds with a JSON object containing the error message.
To handle this case, users can catch the `HttpClientErrorException.NotFound` exception and handle it accordingly in their code.

This ensures that users receive meaningful feedback when encountering errors related to retrieving repository information.

## Endpoints
- `GET /{username}`: Retrieves information about the repositories owned by the specified GitHub user.

## Models
### Repository
- `name`: The name of the repository.
- `ownerLogin`: The login username of the repository owner.
- `fork`: A boolean indicating if the repository is a fork.
- `owner`: An instance of the `Owner` class representing the repository owner.
- `branches`: A list of branch names for the repository.

### RepositoryDetails
- `repoName`: The name of the repository.
- `ownerLogin`: The login username of the repository owner.
- `branches`: A list of branch names for the repository.

### Branch
- `name`: The name of the branch.
- `lastCommitSha`: The SHA of the last commit in the branch.

### Commit
- `sha`: The SHA of the commit.

### Owner
- `login`: The login username of the owner.

## Dependencies
- Spring Boot: Framework for building Java-based applications.
- RestTemplate: HTTP client for making RESTful service calls.
- Spring Web: Provides basic web support.
- Spring Boot Starter Test: Starter for testing Spring Boot applications.

