package de.arthurpicht.meta.git;

import de.arthurpicht.utils.core.strings.Strings;

public class GitRepoUrl {

    private final String url;
    private final String urlRo;

    private final String repoName;

    public GitRepoUrl(String url) {
        this.url = url;
        this.repoName = extractRepoName(url);
        this.urlRo = "";
    }

    public GitRepoUrl(String url, String urlRo) {
        this.url = url;
        this.urlRo = urlRo;
        String repoName = extractRepoName(url);
        String repoNameRo = extractRepoName(urlRo);
        if (!repoName.equals(repoNameRo))
            throw new RuntimeException("repo names not matching for URLs: [" + url + "] != [" + urlRo + "]. Names: [" + repoName + "] != [" + repoNameRo + "].");
        this.repoName = repoName;
    }

    private String extractRepoName(String url) {
        int lastIndexOfSlash = url.lastIndexOf('/');
        int lastIndexOfDot = url.lastIndexOf(".");

        if (lastIndexOfSlash <= 0 || lastIndexOfDot <=0 || !(lastIndexOfDot > lastIndexOfSlash))
            throw new RuntimeException("Error parsing repo name from url.");

        return url.substring(lastIndexOfSlash + 1, lastIndexOfDot);
    }

    public String getUrl() {
        return url;
    }

    public boolean hasUrlReadOnly() {
        return Strings.isSpecified(this.urlRo);
    }

    public String getUrlReadOnly() {
        return this.urlRo;
    }

    public String getRepoName() {
        return repoName;
    }
}
