package de.arthurpicht.meta.helper;

public class GitRepoUrl {

    private final String url;

    private final String repoName;

    public GitRepoUrl(String url) {
        this.url = url;
        this.repoName = extractRepoName();
    }

    private String extractRepoName() {
        int lastIndexOfSlash = this.url.lastIndexOf('/');
        int lastIndexOfDot = this.url.lastIndexOf(".");

        if (lastIndexOfSlash <= 0 || lastIndexOfDot <=0 || !(lastIndexOfDot > lastIndexOfSlash))
            throw new RuntimeException("Error parsing repo name from url.");

        return this.url.substring(lastIndexOfSlash + 1, lastIndexOfDot);
    }

    public String getUrl() {
        return url;
    }

    public String getRepoName() {
        return repoName;
    }
}
