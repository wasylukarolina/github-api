package com.recruitment.githubapi.controller;

import com.recruitment.githubapi.model.Branch;
import com.recruitment.githubapi.model.Commit;
import com.recruitment.githubapi.model.Repository;
import com.recruitment.githubapi.model.RepositoryDetails;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.*;

@RestController
public class GitHubController {

    // Base URL for the GitHub API
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";

    // GitHub authorization token
    //private static final String GITHUB_TOKEN = ""; //Enter your token here

    /**
     * Retrieves the owner's login username for a given repository.
     *
     * @param username        The GitHub username of the repository owner.
     * @param repositoryName  The name of the repository.
     * @param entity          The HTTP entity used for the request.
     * @param restTemplate    The RestTemplate object used for making HTTP requests.
     * @return                The login username of the repository owner, or null if not found.
     */
    private String getOwnerLogin(String username, String repositoryName, HttpEntity<String> entity, RestTemplate restTemplate) {
        String ownerUrl = GITHUB_API_BASE_URL + "/repos/" + username + "/" + repositoryName;
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(ownerUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
        });
        Map<String, Object> repositoryInfo = response.getBody();
        if (repositoryInfo != null && repositoryInfo.containsKey("owner")) {
            Map<String, String> ownerInfo = (Map<String, String>) repositoryInfo.get("owner");
            return ownerInfo.get("login");
        }
        return null;
    }

    /**
     * Retrieves the SHA of the last commit for a given branch in a repository.
     *
     * @param username        The GitHub username of the repository owner.
     * @param repositoryName  The name of the repository.
     * @param branchName      The name of the branch.
     * @param entity          The HTTP entity used for the request.
     * @param restTemplate    The RestTemplate object used for making HTTP requests.
     * @return                The SHA of the last commit in the branch, or null if not found.
     */
    private String getLastCommitSha(String username, String repositoryName, String branchName, HttpEntity<String> entity, RestTemplate restTemplate) {
        String commitsUrl = GITHUB_API_BASE_URL + "/repos/" + username + "/" + repositoryName + "/commits?sha=" + branchName;
        ResponseEntity<Commit[]> commitsResponse = restTemplate.exchange(commitsUrl, HttpMethod.GET, entity, Commit[].class);
        Commit[] commits = commitsResponse.getBody();
        return commits != null && commits.length > 0 ? commits[0].getSha() : null;
    }

    /**
     * Retrieves a list of branch names with their last commit SHA for a given repository.
     *
     * @param username        The GitHub username of the repository owner.
     * @param repositoryName  The name of the repository.
     * @param entity          The HTTP entity used for the request.
     * @param restTemplate    The RestTemplate object used for making HTTP requests.
     * @return                A list of branch names and their last commit SHA, or an empty list if not found.
     */
    private List<String> getBranches(String username, String repositoryName, HttpEntity<String> entity, RestTemplate restTemplate) {
        String branchesUrl = GITHUB_API_BASE_URL + "/repos/" + username + "/" + repositoryName + "/branches";
        ResponseEntity<Branch[]> branchesResponse = restTemplate.exchange(branchesUrl, HttpMethod.GET, entity, Branch[].class);
        Branch[] branches = branchesResponse.getBody();
        List<String> branchDetailsList = new ArrayList<>();
        if (branches != null) {
            for (Branch branch : branches) {
                String branchDetails = branch.getName();
                branchDetails += ", " + getLastCommitSha(username, repositoryName, branch.getName(), entity, restTemplate);
                branchDetailsList.add(branchDetails);
            }
        }
        return branchDetailsList;
    }

    /**
     * Creates a RepositoryDetails object for a given repository.
     *
     * @param username        The GitHub username of the repository owner.
     * @param repository      The Repository object representing the repository.
     * @param entity          The HTTP entity used for the request.
     * @param restTemplate    The RestTemplate object used for making HTTP requests.
     * @return                A RepositoryDetails object containing information about the repository.
     */
    private RepositoryDetails createRepositoryDetails(String username, Repository repository, HttpEntity<String> entity, RestTemplate restTemplate) {
        RepositoryDetails repositoryDetails = new RepositoryDetails();
        repositoryDetails.setRepoName(repository.getName());
        repositoryDetails.setOwnerLogin(getOwnerLogin(username, repository.getName(), entity, restTemplate));
        repositoryDetails.setBranches(getBranches(username, repository.getName(), entity, restTemplate));
        return repositoryDetails;
    }


    /**
     * Retrieves the repositories of a specified GitHub user.
     *
     * @param username The GitHub username of the user whose repositories are to be retrieved.
     * @return A ResponseEntity containing a list of RepositoryDetails objects representing the user's repositories.
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserRepositories(@PathVariable String username) {
        // Construct the URL for retrieving the user's repositories
        String url = GITHUB_API_BASE_URL + "/users/" + username + "/repos";

        // Set up HTTP headers for the request
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "token " + GITHUB_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Create an HTTP entity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Create a RestTemplate object for making HTTP requests
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Send a GET request to the GitHub API to retrieve the repositories
            ResponseEntity<Repository[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Repository[].class);
            Repository[] repositories = response.getBody();

            // Create a list to hold details of non-fork repositories
            List<RepositoryDetails> repositoryDetailsList = new ArrayList<>();

            // Process each repository returned by the API response
            if (repositories != null) {
                for (Repository repository : repositories) {
                    // Check if the repository is not a fork
                    if (!repository.isFork()) {
                        // Create RepositoryDetails object for the current repository
                        RepositoryDetails repositoryDetails = createRepositoryDetails(username, repository, entity, restTemplate);

                        // Add the RepositoryDetails object to the list
                        repositoryDetailsList.add(repositoryDetails);
                    }
                }
            }
            // Return a ResponseEntity with a list of RepositoryDetails objects
            return ResponseEntity.ok(repositoryDetailsList);

            // Handle the case where the specified user is not found
        } catch (HttpClientErrorException.NotFound e) {
            int responseCode = HttpStatus.NOT_FOUND.value();
            String whyHasItHappened = username + " " + HttpStatus.NOT_FOUND.getReasonPhrase();

            // Create a JSON response body with the error message
            String responseBody = "{\n" +
                    "  \"status\": " + responseCode + ",\n" +
                    "  \"message\": \"" + whyHasItHappened + "\"\n" +
                    "}";

            // Return a ResponseEntity with the error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }
}