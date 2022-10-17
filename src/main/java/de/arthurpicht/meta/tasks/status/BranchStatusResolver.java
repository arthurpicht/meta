package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.utils.io.nio2.FileUtils;

import java.nio.file.Path;

public class BranchStatusResolver {

    public static BranchStatus resolve(Path repoPath, String repoName) throws GitException {

        if (!FileUtils.isExistingDirectory(repoPath)) {
            throw new MetaRuntimeException("Repo [" + repoPath.toAbsolutePath() + "] not found. Consider calling clone.");
        }

        if (!Git.isGitRepo(repoPath)) {
            return BranchStatus.getInstanceNoRepo(repoName);
        }

        String currentBranchName = Git.getCurrentBranch(repoPath);
        boolean hasUncommittedChanges = Git.hasUncommittedChanges(repoPath);
        boolean hasUnpushedCommits = Git.hasUnpushedCommits(repoPath);
        boolean hasStash = Git.hasStash(repoPath);
        boolean hasCommitsAhead = Git.hasCommitsAhead(repoPath, currentBranchName);

        return BranchStatus.getInstanceRepo(repoName, currentBranchName, hasUncommittedChanges, hasUnpushedCommits, hasStash, hasCommitsAhead);
    }

}
