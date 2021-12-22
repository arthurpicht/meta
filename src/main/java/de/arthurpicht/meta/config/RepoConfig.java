package de.arthurpicht.meta.config;

import de.arthurpicht.meta.cli.target.Targets;
import de.arthurpicht.meta.git.GitRepoUrl;
import de.arthurpicht.utils.core.strings.Strings;

import java.nio.file.Path;

public class RepoConfig {

    private final String repoId;
    private final GitRepoUrl gitRepoUrl;
    private final Path destinationPath;
    private final String repoName;
    private final boolean isRepoNameAltered;
    private final String branch;
    private final Targets targets;

    public RepoConfig(String repoId, GitRepoUrl gitRepoUrl, Path destinationPath, String repoName, boolean isRepoNameAltered, String branch, Targets targets) {
        this.repoId = repoId;
        this.gitRepoUrl = gitRepoUrl;
        this.destinationPath = destinationPath;
        this.repoName = repoName;
        this.isRepoNameAltered = isRepoNameAltered;
        this.branch = branch;
        this.targets = targets;
    }

    public String getRepoId() {
        return this.repoId;
    }

    public String getGitRepoUrl() {
        return this.gitRepoUrl.getUrl();
    }

    public boolean hasGitRepoUrlReadOnly() {
        return this.gitRepoUrl.hasUrlReadOnly();
    }

    public String getGitRepoUrlReadOnly() {
        return this.gitRepoUrl.getUrlReadOnly();
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

    public Targets getTargets() {
        return this.targets;
    }

    public boolean hasTarget(String targetName) {
        return this.targets.hasTarget(targetName);
    }

}
