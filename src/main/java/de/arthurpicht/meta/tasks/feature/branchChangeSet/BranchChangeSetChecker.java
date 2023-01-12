package de.arthurpicht.meta.tasks.feature.branchChangeSet;

import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.config.RepoConfigs;
import de.arthurpicht.utils.core.strings.Strings;

import java.util.List;

public class BranchChangeSetChecker {

    public static void check(BranchChangeSet branchChangeSet, boolean force) throws CommandExecutorException {

        if (force && branchChangeSet.hasUncommittedChangedRepos()) {
            List<String> uncommittedChangedRepoNames
                    = RepoConfigs.getRepoNames(branchChangeSet.getUncommittedChangedRepos());
            System.out.println(
                    "The following repos are affected and have uncommitted changes: "
                            + Strings.listing(uncommittedChangedRepoNames, ", ") + ".\n"
                            + "Force checkout.");
        }

        if (isBlocked(branchChangeSet)) {
            if (force) {
                List<String> modifiedFilesRepoNames
                        = RepoConfigs.getRepoNames(branchChangeSet.getModifiedFilesRepos());
                throw new CommandExecutorException("There are modified files in the following repos: "
                        + Strings.listing(modifiedFilesRepoNames, ", ") + ".");
            } else {
                List<String> uncommittedChangedRepoNames
                        = RepoConfigs.getRepoNames(branchChangeSet.getUncommittedChangedRepos());
                throw new CommandExecutorException(
                        "Cannot checkout feature. The following repos are affected and have uncommitted changes: "
                                + Strings.listing(uncommittedChangedRepoNames, ", ") + ".\n"
                                + "Consider calling option --force.");
            }
        }

    }

    public static boolean isBlocked(BranchChangeSet branchChangeSet) {
        if (branchChangeSet.isForce()) {
            return branchChangeSet.hasModifiedFilesRepos();
        } else {
            return branchChangeSet.hasUncommittedChangedRepos();
        }
    }

}
