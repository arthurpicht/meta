package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.Const;
import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureInventory;
import de.arthurpicht.utils.io.nio2.FileUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class RepoProperties {

    private final RepoConfig repoConfig;
    private final Boolean isRepoPathExisting;
    private final Feature feature;
    private Boolean isRepo = null;
    private String currentBranchName = null;
    private String defaultBranchName = null;
    private Boolean hasUncommittedChanges = null;
    private Boolean hasUnpushedCommits = null;
    private Boolean hasStash = null;
    private Boolean hasCommitsAhead = null;

    public RepoProperties(RepoConfig repoConfig, Feature feature) {
        this.repoConfig = repoConfig;
        this.isRepoPathExisting = FileUtils.isExistingDirectory(this.repoConfig.getRepoPath());
        this.feature = feature;
    }

    public String getRepoName() {
        return repoConfig.getRepoName();
    }

    public Path getRepoPath() {
        return repoConfig.getRepoPath();
    }

    public boolean isRepoPathExisting() {
        return this.isRepoPathExisting;
    }

    public boolean isRepo() {
        if (this.isRepo == null) {
            this.isRepo = Git.isGitRepo(getRepoPath());
        }
        return isRepo;
    }

    public String getCurrentBranchName() throws GitException {
        assertExistingRepo();
        if (this.currentBranchName == null)
            this.currentBranchName = Git.getCurrentBranch(getRepoPath());
        return this.currentBranchName;
    }

    public String getDefaultBranchName() throws GitException {
        assertExistingRepo();
        if (this.defaultBranchName == null)
            this.defaultBranchName = Git.getDefaultBranch(getRepoPath());
        return this.defaultBranchName;
    }

    public String getBaseBranchName() throws GitException {
        assertExistingRepo();
        if (this.repoConfig.hasAlteredBranch()) {
            return this.repoConfig.getBranch();
        } else {
            return getDefaultBranchName();
        }
    }

//    public String getIntendedBranchName() {
//        assertExistingRepo();
//        if (this.feature.hasFeature())
//    }

    public boolean hasUncommittedChanges() throws GitException {
        assertExistingRepo();
        if (this.hasUncommittedChanges == null)
            this.hasUncommittedChanges = Git.hasUncommittedChanges(getRepoPath());
        return this.hasUncommittedChanges;
    }

    public boolean hasUnpushedCommits() throws GitException {
        assertExistingRepo();
        if (this.hasUnpushedCommits == null)
            this.hasUnpushedCommits = Git.hasUnpushedCommits(getRepoPath());
        return this.hasUnpushedCommits;
    }

    public boolean hasStash() throws GitException {
        assertExistingRepo();
        if (this.hasStash == null)
            this.hasStash = Git.hasStash(getRepoPath());
        return this.hasStash;
    }

    public boolean hasCommitsAhead() throws GitException {
        assertExistingRepo();
        if (this.hasCommitsAhead == null)
            this.hasCommitsAhead = Git.hasCommitsAhead(getRepoPath(), this.getCurrentBranchName());
        return this.hasCommitsAhead;
    }

    private void assertExistingRepo() {
        if (!this.isRepoPathExisting)
            throw new IllegalStateException("Repo path not existing.");
        if (!isRepo())
            throw new IllegalStateException("Not a git repo.");
    }

    private List<String> getFeatureBranches() throws GitException {
        List<String> localBranches = Git.getLocalBranches(getRepoPath());
        return localBranches.stream()
                .filter(brachName -> brachName.startsWith(Const.FEATURE_BRANCH_PREFIX))
                .collect(Collectors.toList());
    }

}
