package de.arthurpicht.meta.tasks.status;

public class BranchStatus {

    private final String repoName;
    private final boolean isRepo;
    private final String currentBranchName;
    private final boolean hasUncommittedChanges;
    private final boolean hasUnpushedCommits;
    private final boolean hasStash;
    private final boolean hasCommitsAhead;

    private BranchStatus(
            String repoName,
            boolean isRepo,
            String currentBranchName,
            boolean hasUncommittedChanges,
            boolean hasUnpushedCommits,
            boolean hasStash,
            boolean hasCommitsAhead) {
        this.repoName = repoName;
        this.isRepo = isRepo;
        this.currentBranchName = currentBranchName;
        this.hasUncommittedChanges = hasUncommittedChanges;
        this.hasUnpushedCommits = hasUnpushedCommits;
        this.hasStash = hasStash;
        this.hasCommitsAhead = hasCommitsAhead;
    }

    public static BranchStatus getInstanceRepo(
            String repoName, String currentBranchName, boolean hasUncommittedChanges, boolean hasUnpushedCommits,
            boolean hasStash, boolean hasCommitsAhead
    ) {
        return new BranchStatus(repoName, true, currentBranchName, hasUncommittedChanges, hasUnpushedCommits, hasStash, hasCommitsAhead);
    }

    public static BranchStatus getInstanceNoRepo(String repoName) {
        return new BranchStatus(repoName, false, "", false, false, false, false);
    }

    public String getRepoName() {
        return repoName;
    }

    public String getCurrentBranchName() {
        return this.currentBranchName;
    }

    public boolean isRepo() {
        return isRepo;
    }

    public boolean hasUncommittedChanges() {
        return hasUncommittedChanges;
    }

    public boolean hasUnpushedCommits() {
        return hasUnpushedCommits;
    }

    public boolean hasStash() {
        return hasStash;
    }

    public boolean hasCommitsAhead() {
        return this.hasCommitsAhead;
    }

    public boolean isStatusGreen() {
        return this.isRepo && !hasUncommittedChanges && !hasUnpushedCommits && !hasCommitsAhead && !hasStash;
    }

    public boolean isStatusYellow() {
        return this.isRepo && !hasUncommittedChanges && !hasUnpushedCommits && !hasCommitsAhead && hasStash;
    }

}
