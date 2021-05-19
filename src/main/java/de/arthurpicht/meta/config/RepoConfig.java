package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.meta.helper.GitRepoUrl;
import de.arthurpicht.utils.core.strings.Strings;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RepoConfig {

    private static final String KEY_URL = "url";
    private static final String KEY_DESTINATION_DIR = "destinationDir";
    private static final String KEY_REPO_NAME = "repoName";
    private static final String KEY_BRANCH = "branch";

    private final String repoId;
    private final GitRepoUrl gitRepoUrl;
    private final Path destinationPath;
    private final String repoName;
    private final boolean isRepoNameAltered;
    private final String branch;

    public RepoConfig(Configuration configuration, Path referencePath) throws ConfigurationException {
        this.repoId = configuration.getSectionName();

        ConfigHelper.assertKey(configuration, this.repoId, KEY_URL);
        String urlString = configuration.getString(KEY_URL);
        this.gitRepoUrl = new GitRepoUrl(urlString);

        if (configuration.containsKey(KEY_DESTINATION_DIR)) {
            Path destinationDirPath = Paths.get(configuration.getString(KEY_DESTINATION_DIR));
            if (destinationDirPath.isAbsolute()) {
                this.destinationPath = destinationDirPath;
            } else {
                this.destinationPath = referencePath.resolve(destinationDirPath);
            }
            this.destinationPath.normalize().toAbsolutePath();
        } else {
            this.destinationPath = referencePath;
        }

        if (configuration.containsKey(KEY_REPO_NAME)) {
            this.repoName = configuration.getString(KEY_REPO_NAME);
        } else {
            this.repoName = this.gitRepoUrl.getRepoName();
        }

        this.isRepoNameAltered = !this.repoName.equals(this.gitRepoUrl.getRepoName());

        if (configuration.containsKey(KEY_BRANCH)) {
            this.branch = configuration.getString(KEY_BRANCH);
        } else {
            this.branch = "";
        }
    }

    public String getRepoId() {
        return this.repoId;
    }

    public String getGitRepoUrl() {
        return this.gitRepoUrl.getUrl();
    }

    public Path getDestinationPath() {
        return this.destinationPath;
    }

    public boolean hasAlteredRepoName() {
        return this.isRepoNameAltered;
    }

    public String getRepoName() {
        return repoName;
    }

    public boolean hasAlteredBranch() {
        return Strings.isSpecified(this.branch);
    }

    public String getBranch() {
        return this.branch;
    }

    public Path getRepoPath() {
        return this.destinationPath.resolve(this.repoName);
    }

}
