package de.arthurpicht.meta.cli.feature.branchChangeSet;

import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;

import java.util.List;

public class BranchChangeSet {

    private final List<RepoConfig> checkoutToBaseBranches;
    private final List<RepoConfig> checkoutNewFeatureBranches;
    private final List<RepoConfig> uncommittedChangesBlockingBranches;
    private final List<RepoConfig> modifiedFilesBlockingBranches;
    private final boolean force;

    public static BranchChangeSet create(
            FeatureInfo sourceFeatureInfo,
            FeatureInfo destinationFeatureInfo,
            boolean force
    ) {
        return BranchChangeSetCreator.create(
                sourceFeatureInfo,
                destinationFeatureInfo,
                force
        );
    }

    BranchChangeSet(
            List<RepoConfig> checkoutToBaseBranches,
            List<RepoConfig> checkoutNewFeatureBranches,
            List<RepoConfig> uncommittedChangesBlockingBranches,
            List<RepoConfig> modifiedFilesBlockingBranches,
            boolean force
    ) {
        this.checkoutToBaseBranches = checkoutToBaseBranches;
        this.checkoutNewFeatureBranches = checkoutNewFeatureBranches;
        this.uncommittedChangesBlockingBranches = uncommittedChangesBlockingBranches;
        this.modifiedFilesBlockingBranches = modifiedFilesBlockingBranches;
        this.force = force;
    }

    public List<RepoConfig> getCheckoutToBaseBranches() {
        return this.checkoutToBaseBranches;
    }

    public List<RepoConfig> getCheckoutNewFeatureBranches() {
        return this.checkoutNewFeatureBranches;
    }

    public List<RepoConfig> getUncommittedChangesBlockingBranches() {
        return this.uncommittedChangesBlockingBranches;
    }

    public boolean hasUncommittedChangesBlockingBranches() {
        return !this.uncommittedChangesBlockingBranches.isEmpty();
    }

    public List<RepoConfig> getModifiedFilesBlockingBranches() {
        return this.modifiedFilesBlockingBranches;
    }

    public boolean isForce() {
        return this.force;
    }

    public boolean isBlocked() {
        if (this.force) {
            return !this.modifiedFilesBlockingBranches.isEmpty();
        } else {
            return !this.uncommittedChangesBlockingBranches.isEmpty();
        }
    }

}
