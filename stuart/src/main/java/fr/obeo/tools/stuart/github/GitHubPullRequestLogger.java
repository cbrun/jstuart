package fr.obeo.tools.stuart.github;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.spotify.github.v3.clients.GitHubClient;
import com.spotify.github.v3.prs.requests.ImmutablePullRequestParameters;
import com.spotify.github.v3.prs.requests.PullRequestParameters;

import fr.obeo.tools.stuart.Post;

public class GitHubPullRequestLogger {
    private static final String GITHUB_PR_ICON = "https://raw.githubusercontent.com/primer/octicons/main/icons/git-pull-request-24.svg";

    private final String token;

    private final GitHubClient githubClient;
    
    public GitHubPullRequestLogger(String token) {
        this.token = Objects.requireNonNull(token);
        this.githubClient = GitHubClient.create(URI.create("https://api.github.com/"), this.token);
    }

    public Collection<Post> getPatchsets(String orgName, String repoName) {
        List<Post> posts = new ArrayList<>();
        var prClient = githubClient.createRepositoryClient(orgName, repoName).createPullRequestClient();
        PullRequestParameters parameters = ImmutablePullRequestParameters.builder().state("open").build();
        prClient.list(parameters).join().forEach(pr -> {
            String url = pr.htmlUrl().toString();
            Date createdAt = new Date(pr.createdAt().instant().toEpochMilli());
            Post newPost = Post.createPost(url, pr.title(), pr.user().login(), GITHUB_PR_ICON, createdAt);
            newPost.setQuote(false);
            newPost.mightBeTruncated(false);
            posts.add(newPost);
        });
        return posts;
    }
}
