package de.arthurpicht.meta.cli.feature.branchChangeSet;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.config.RepoConfig;

import java.util.List;

public class BranchChangeSetDebugOut {

    public static void debugOut(BranchChangeSet branchChangeSet) {

        if (!ExecutionContext.isDebug()) return;

        System.out.println(BranchChangeSet.class.getSimpleName() + ":");

        System.out.println("    checkout to base branch:");
        List<RepoConfig> checkoutToBaseBranch = branchChangeSet.getCheckoutToBaseBranches();
        if (checkoutToBaseBranch.isEmpty())
            System.out.println("        <none>");
        for (RepoConfig repoConfig : checkoutToBaseBranch) {
            System.out.println("        " + repoConfig.getRepoName());
        }

        System.out.println("    checkout to new feature branch:");
        List<RepoConfig> checkoutToNewFeatureBranch = branchChangeSet.getCheckoutNewFeatureBranches();
        if (checkoutToNewFeatureBranch.isEmpty())
            System.out.println("        <none>");
        for (RepoConfig repoConfig : checkoutToNewFeatureBranch) {
            System.out.println("        " + repoConfig.getRepoName());
        }

        System.out.println("    repos to be changed but with uncommitted changes:");
        List<RepoConfig> uncommittedChanges = branchChangeSet.getUncommittedChangesBlockingBranches();
        if (uncommittedChanges.isEmpty())
            System.out.println("        <none>");
        for (RepoConfig repoConfig : uncommittedChanges) {
            System.out.println("        " + repoConfig.getRepoName());
        }

        System.out.println("    repos to be changed with uncommitted changes containing modified files:");
        List<RepoConfig> modifiedFilesRepos = branchChangeSet.getModifiedFilesBlockingBranches();
        if (modifiedFilesRepos.isEmpty())
            System.out.println("        <none>");
        for (RepoConfig repoConfig : modifiedFilesRepos) {
            System.out.println("        " + repoConfig.getRepoName());
        }

        System.out.println("    force checkout? " + branchChangeSet.isForce());

        System.out.println("    isBlocked? " + branchChangeSet.isBlocked());
    }

}
