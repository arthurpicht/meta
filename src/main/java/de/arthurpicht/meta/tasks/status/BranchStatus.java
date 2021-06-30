package de.arthurpicht.meta.tasks.status;

public class BranchStatus {

    private final String repoName;
    private final boolean isRepo;
    private final String currentBranchName;
    private final boolean hasUncommitedChanges;
    private final boolean hasUnpushedCommits;
    private final boolean hasStash;

    private BranchStatus(String repoName, boolean isRepo, String currentBranchName, boolean hasUncommitedChanges, boolean hasUnpushedCommits, boolean hasStash) {
        this.repoName = repoName;
        this.isRepo = isRepo;
        this.currentBranchName = currentBranchName;
        this.hasUncommitedChanges = hasUncommitedChanges;
        this.hasUnpushedCommits = hasUnpushedCommits;
        this.hasStash = hasStash;
    }

    public static BranchStatus getInstanceRepo(
            String repoName, String currentBranchName, boolean hasUncommitedChanges, boolean hasUnpushedCommits,
            boolean hasStash
    ) {
        return new BranchStatus(repoName, true, currentBranchName, hasUncommitedChanges, hasUnpushedCommits, hasStash);
    }

    public static BranchStatus getInstanceNoRepo(String repoName) {
        return new BranchStatus(repoName, false, "", false, false, false);
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

    public boolean hasUncommitedChanges() {
        return hasUncommitedChanges;
    }

    public boolean hasUnpushedCommits() {
        return hasUnpushedCommits;
    }

    public boolean hasStash() {
        return hasStash;
    }

    public boolean isStatusGreen() {
        return this.isRepo && !hasUncommitedChanges && !hasUnpushedCommits && !hasStash;
    }

    public boolean isStatusYellow() {
        return this.isRepo && !hasUncommitedChanges && !hasUnpushedCommits && hasStash;
    }

}
