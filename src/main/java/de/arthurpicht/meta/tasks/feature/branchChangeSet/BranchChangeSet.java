package de.arthurpicht.meta.tasks.feature.branchChangeSet;

import de.arthurpicht.meta.config.RepoConfig;

import java.util.List;

public class BranchChangeSet {

    private final List<RepoConfig> checkoutToBaseRepos;
    private final List<RepoConfig> checkoutToNewFeatureRepos;
    private final List<RepoConfig> uncommittedChangedRepos;
    private final List<RepoConfig> modifiedFilesRepos;
    private final boolean force;

    BranchChangeSet(
            List<RepoConfig> checkoutToBaseRepos,
            List<RepoConfig> checkoutToNewFeatureRepos,
            List<RepoConfig> uncommittedChangedRepos,
            List<RepoConfig> modifiedFilesRepos,
            boolean force
    ) {
        this.checkoutToBaseRepos = checkoutToBaseRepos;
        this.checkoutToNewFeatureRepos = checkoutToNewFeatureRepos;
        this.uncommittedChangedRepos = uncommittedChangedRepos;
        this.modifiedFilesRepos = modifiedFilesRepos;
        this.force = force;
    }

    public List<RepoConfig> getCheckoutToBaseRepos() {
        return this.checkoutToBaseRepos;
    }

    public List<RepoConfig> getCheckoutToNewFeatureRepos() {
        return this.checkoutToNewFeatureRepos;
    }

    public List<RepoConfig> getUncommittedChangedRepos() {
        return this.uncommittedChangedRepos;
    }

    public boolean hasUncommittedChangedRepos() {
        return !this.uncommittedChangedRepos.isEmpty();
    }

    public boolean hasModifiedFilesRepos() {
        return !this.modifiedFilesRepos.isEmpty();
    }

    public List<RepoConfig> getModifiedFilesRepos() {
        return this.modifiedFilesRepos;
    }

    public boolean isForce() {
        return this.force;
    }

}
