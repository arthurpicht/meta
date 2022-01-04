package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.helper.FilesHelper;

import java.nio.file.Path;

public class BranchStatusResolver {

    public static BranchStatus resolve(Path repoPath, String repoName) throws GitException {

        if (!FilesHelper.isExistingDirectory(repoPath)) {
            throw new MetaRuntimeException("Repo [" + repoPath.toAbsolutePath() + "] not found. Consider calling clone.");
        }

        if (!Git.isGitRepo(repoPath)) {
            return BranchStatus.getInstanceNoRepo(repoName);
        }

        String currentBranchName = Git.getCurrentBranch(repoPath);
        boolean hasUncommitedChanges = Git.hasUncommitedChanges(repoPath);
        boolean hasUnpushedCommits = Git.hasUnpushedCommits(repoPath);
        boolean hasStash = Git.hasStash(repoPath);
        boolean hasCommitsAhead = Git.hasCommitsAhead(repoPath, currentBranchName);

        return BranchStatus.getInstanceRepo(repoName, currentBranchName, hasUncommitedChanges, hasUnpushedCommits, hasStash, hasCommitsAhead);
    }

}
