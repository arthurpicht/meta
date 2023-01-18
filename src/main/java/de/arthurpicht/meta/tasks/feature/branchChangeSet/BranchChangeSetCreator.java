package de.arthurpicht.meta.tasks.feature.branchChangeSet;

import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.git.Repos;
import de.arthurpicht.meta.helper.ListHelper;
import de.arthurpicht.meta.tasks.feature.FeatureBranchName;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;
import de.arthurpicht.meta.tasks.status.RepoProperties;

import java.util.ArrayList;
import java.util.List;

public class BranchChangeSetCreator {

    public static BranchChangeSet create(
            FeatureInfo sourceFeatureInfo,
            FeatureInfo destinationFeatureInfo,
            boolean force)
            throws CommandExecutorException {

        List<RepoConfig> checkoutToBaseBranches = new ArrayList<>();
        List<RepoConfig> checkoutNewFeatureBranches = new ArrayList<>();
        List<RepoConfig> uncommittedChangesBlockingBranches = new ArrayList<>();
        List<RepoConfig> modifiedFilesBlockingBranches = new ArrayList<>();

        FeatureBranchName destinationBranchName = destinationFeatureInfo.getFeature().getFeatureBranchName();
        boolean equalsFeature = equalsFeature(sourceFeatureInfo, destinationFeatureInfo);

        List<RepoConfig> affectedRepos = determineAffectedRepos(sourceFeatureInfo, destinationFeatureInfo);

        for (RepoConfig repoConfig : affectedRepos) {
            consistencyCheck(repoConfig, destinationFeatureInfo);
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
            } else if (!equalsFeature) {
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

    private static boolean equalsFeature(FeatureInfo sourceFeatureInfo, FeatureInfo destinationFeatureInfo) {
        if (!sourceFeatureInfo.hasFeature()) return false;
        return sourceFeatureInfo.getFeature().getName().equals(destinationFeatureInfo.getFeature().getName());
    }

    private static void consistencyCheck(RepoConfig repoConfig, FeatureInfo destinationFeatureInfo) throws CommandExecutorException {
        RepoProperties repoProperties = new RepoProperties(repoConfig, destinationFeatureInfo);
        if (!repoProperties.isRepoPathExisting())
            throw new CommandExecutorException(
                    "Repo [" + repoProperties.getRepoName() + "] not found as expected here: ["
                            + repoProperties.getRepoPath().toAbsolutePath() + "]. "
                            + "Consider calling clone.");

        if (!repoProperties.isRepo()) {
            throw new CommandExecutorException("No git repo at [" + repoProperties.getRepoPath().toAbsolutePath() + "].");
        }
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
