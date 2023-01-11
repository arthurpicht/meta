package de.arthurpicht.meta.cli.feature.branchChangeSet;

import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.git.Repos;
import de.arthurpicht.meta.helper.ListHelper;
import de.arthurpicht.meta.tasks.feature.FeatureBranchName;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;

import java.util.ArrayList;
import java.util.List;

public class BranchChangeSetCreator {

    public static BranchChangeSet create(
            FeatureInfo sourceFeatureInfo,
            FeatureInfo destinationFeatureInfo,
            boolean force
    ) {
        List<RepoConfig> checkoutToBaseBranches = new ArrayList<>();
        List<RepoConfig> checkoutNewFeatureBranches = new ArrayList<>();
        List<RepoConfig> uncommittedChangesBlockingBranches = new ArrayList<>();
        List<RepoConfig> modifiedFilesBlockingBranches = new ArrayList<>();

        FeatureBranchName destinationBranchName = destinationFeatureInfo.getFeature().getFeatureBranchName();
        boolean featureIdentity = sourceFeatureInfo.getFeature().getName().equals(destinationFeatureInfo.getFeature().getName());

        List<RepoConfig> affectedRepos = determineAffectedRepos(sourceFeatureInfo, destinationFeatureInfo);

        for (RepoConfig repoConfig : affectedRepos) {
            if (isOnDestinationBranch(repoConfig, destinationBranchName)) continue;
            if (hasUncommittedChanges(repoConfig)) {
                if (force) {
                    if (hasModifiedFiles(repoConfig)) {
                        uncommittedChangesBlockingBranches.add(repoConfig);
                        modifiedFilesBlockingBranches.add(repoConfig);
                        continue;
                    }
                } else {
                    uncommittedChangesBlockingBranches.add(repoConfig);
                    continue;
                }
            }

            if (hasDestinationFeatureBranch(repoConfig, destinationFeatureInfo)) {
                checkoutNewFeatureBranches.add(repoConfig);
            } else if (!featureIdentity) {
                checkoutToBaseBranches.add(repoConfig);
            }
        }

        return new BranchChangeSet(
                checkoutToBaseBranches,
                checkoutNewFeatureBranches,
                uncommittedChangesBlockingBranches,
                modifiedFilesBlockingBranches,
                force);
    }

    private static boolean hasDestinationFeatureBranch(RepoConfig repoConfig, FeatureInfo destinationFeatureInfo) {
        FeatureBranchName destinationFeatureBranchName = destinationFeatureInfo.getFeature().getFeatureBranchName();
        return destinationFeatureInfo.containsRepoWithFeature(repoConfig.getRepoName(), destinationFeatureBranchName);
    }

    private static boolean isOnDestinationBranch(RepoConfig repoConfig, FeatureBranchName destinationBranchName) {
        return Repos.isOnBranch(repoConfig.getRepoPath(), destinationBranchName.getBranchName());
    }

    private static boolean hasUncommittedChanges(RepoConfig repoConfig) {
        try {
            return Git.hasUncommittedChanges(repoConfig.getRepoPath());
        } catch (GitException e) {
            throw new MetaRuntimeException(e.getMessage(), e);
        }
    }

    private static boolean hasModifiedFiles(RepoConfig repoConfig) {
        try {
            return Git.hasModifiedFiles(repoConfig.getRepoPath());
        } catch (GitException e) {
            throw new MetaRuntimeException(e.getMessage(), e);
        }
    }

    private static List<RepoConfig> determineAffectedRepos(
            FeatureInfo sourceFeatureInfo, FeatureInfo destinationFeatureInfo) {

        List<RepoConfig> affectedRepos = new ArrayList<>(sourceFeatureInfo.getRelatedRepoConfigs());
        affectedRepos = ListHelper.addIfNotYetContained(affectedRepos, destinationFeatureInfo.getRelatedRepoConfigs());

        return affectedRepos;
    }

}
