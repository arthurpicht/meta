package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;

import java.nio.file.Path;

public class BranchStatusResolver {

    public static BranchStatus resolve(Path repoPath, String repoName) throws GitException {

        if (!Git.isGitRepo(repoPath)) {
            return BranchStatus.getInstanceNoRepo(repoName);
        }

        String currentBranchName = Git.getCurrentBranch(repoPath);
        boolean hasUncommitedChanges = Git.hasUncommitedChanges(repoPath);
        boolean hasUnpushedCommits = Git.hasUnpushedCommits(repoPath);
        boolean hasStash = Git.hasStash(repoPath);

        return BranchStatus.getInstanceRepo(repoName, currentBranchName, hasUncommitedChanges, hasUnpushedCommits, hasStash);
    }

}
