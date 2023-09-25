package de.arthurpicht.meta.config;

import de.arthurpicht.meta.config.exceptions.ConfigurationException;
import de.arthurpicht.utils.core.strings.Strings;

public class GitRepoUrl {

    private final String url;
    private final String urlRo;

    private final String repoName;

    public GitRepoUrl(String url) throws ConfigurationException {
        this.url = url;
        this.repoName = extractRepoName(url);
        this.urlRo = "";
    }

    public GitRepoUrl(String url, String urlRo) throws ConfigurationException {
        this.url = url;
        this.urlRo = urlRo;
        String repoName = extractRepoName(url);
        String repoNameRo = extractRepoName(urlRo);
        if (!repoName.equals(repoNameRo))
            throw new ConfigurationException("repo names not matching for URLs: [" + url + "] != [" + urlRo + "]. Names: [" + repoName + "] != [" + repoNameRo + "].");
        this.repoName = repoName;
    }

    private String extractRepoName(String url) throws ConfigurationException {
        if (url.endsWith(".git")) {
            return extractRepoNameDotGit(url);
        } else {
            return extractRepoNameNoDotGit(url);
        }
    }

    private String extractRepoNameDotGit(String url) throws ConfigurationException {
        int lastIndexOfSlash = url.lastIndexOf('/');
        int lastIndexOfDot = url.lastIndexOf(".");

        if (lastIndexOfSlash <= 0 || lastIndexOfDot <=0 || !(lastIndexOfDot > lastIndexOfSlash))
            throw new ConfigurationException("Error parsing repo name from url: [" + url + "].");

        return url.substring(lastIndexOfSlash + 1, lastIndexOfDot);
    }

    private String extractRepoNameNoDotGit(String url) throws ConfigurationException {
        // This applies to bitbucket wiki repos
        int lastIndexOfSlash = url.lastIndexOf('/');
        int lastIndexOfDot = url.lastIndexOf(".");

        if (url.endsWith("/") || lastIndexOfSlash <= 0 || lastIndexOfDot > lastIndexOfSlash)
            throw new ConfigurationException("Error parsing repo name from url: [" + url + "].");

        return url.substring(lastIndexOfSlash + 1);
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
