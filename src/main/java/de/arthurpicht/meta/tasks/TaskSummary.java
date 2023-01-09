package de.arthurpicht.meta.tasks;

import java.util.ArrayList;
import java.util.List;

public class TaskSummary {

    private final List<String> reposSuccess;
    private final List<String> reposSkip;
    private final List<String> reposWarning;
    private final List<String> reposFailed;

    public TaskSummary() {
        this.reposSuccess = new ArrayList<>();
        this.reposSkip = new ArrayList<>();
        this.reposWarning = new ArrayList<>();
        this.reposFailed = new ArrayList<>();
    }

    public void addRepoSuccess(String repoName) {
        this.reposSuccess.add(repoName);
    }

    public void addRepoSkip(String repoName) {
        this.reposSkip.add(repoName);
    }

    public void addRepoWarning(String repoName) {
        this.reposWarning.add(repoName);
    }

    public void addRepoFailed(String repoName) {
        this.reposFailed.add(repoName);
    }

    public int getNrOfReposSuccess() {
        return this.reposSuccess.size();
    }

    public boolean hasReposWithSuccess() {
        return getNrOfReposSuccess() > 0;
    }

    public int getNrOfReposSkipped() {
        return this.reposSkip.size();
    }

    public boolean hasReposThatWereSkipped() {
        return getNrOfReposSkipped() > 0;
    }

    public int getNrOfReposWithWarning() {
        return this.reposWarning.size();
    }

    public boolean hasReposWithWarning() {
        return this.getNrOfReposWithWarning() > 0;
    }

    public int getNrOfReposFailed() {
        return this.reposFailed.size();
    }

    public boolean hasReposFailed() {
        return getNrOfReposFailed() > 0;
    }

    public List<String> getReposSuccess() {
        return this.reposSuccess;
    }

    public List<String> getReposWarning() {
        return this.reposWarning;
    }

    public List<String> getReposFailed() {
        return this.reposFailed;
    }

    public boolean hasSuccess() {
        return this.reposFailed.isEmpty();
    }

    public int getNumberOfRepos() {
        return getNrOfReposSuccess() + getNrOfReposSkipped() + getNrOfReposWithWarning() + getNrOfReposFailed();
    }

}
