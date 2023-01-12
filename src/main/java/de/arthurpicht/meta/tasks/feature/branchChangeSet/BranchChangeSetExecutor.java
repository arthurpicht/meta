package de.arthurpicht.meta.tasks.feature.branchChangeSet;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.helper.DebugOut;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;
import de.arthurpicht.meta.tasks.status.RepoProperties;

public class BranchChangeSetExecutor {

    public static void execute(
            BranchChangeSet branchChangeSet,
            FeatureInfo sourceFeatureInfo,
            FeatureInfo destinationFeatureInfo) {

        checkoutBase(branchChangeSet, sourceFeatureInfo);
        checkoutFeature(branchChangeSet, destinationFeatureInfo);
    }

    private static void checkoutBase(BranchChangeSet branchChangeSet, FeatureInfo sourceFeatureInfo) {
        for (RepoConfig repoConfig : branchChangeSet.getCheckoutToBaseRepos()) {
            DebugOut.println("checkout base branch on repo [" + repoConfig.getRepoName() + "].");
            RepoProperties repoProperties = new RepoProperties(repoConfig, sourceFeatureInfo);
            try {
                Git.checkout(repoProperties.getRepoPath(), repoProperties.getBaseBranchName(), ExecutionContext.isDebug());
            } catch (GitException e) {
                throw new MetaRuntimeException(e.getMessage(), e);
            }
        }
    }

    private static void checkoutFeature(BranchChangeSet branchChangeSet, FeatureInfo destinationFeatureInfo) {
        String featureBranchName = destinationFeatureInfo.getFeature().getFeatureBranchName().getBranchName();
        for (RepoConfig repoConfig : branchChangeSet.getCheckoutToNewFeatureRepos()) {
            DebugOut.println("checkout featureBranch [" + featureBranchName + "] on repo " +
                    "[" + repoConfig.getRepoName() + "].");
            RepoProperties repoProperties = new RepoProperties(repoConfig, destinationFeatureInfo);
            try {
                Git.checkout(repoProperties.getRepoPath(), repoProperties.getIntendedBranchName(), ExecutionContext.isDebug());
            } catch (GitException e) {
                throw new MetaRuntimeException(e.getMessage(), e);
            }
        }
    }

}
