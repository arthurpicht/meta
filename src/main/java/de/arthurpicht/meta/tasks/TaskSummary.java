package de.arthurpicht.meta.tasks;

import java.util.ArrayList;
import java.util.List;

public class TaskSummary {

    private final List<String> reposSuccess;
    private final List<String> reposWarning;
    private final List<String> reposFailed;

    public TaskSummary() {
        this.reposSuccess = new ArrayList<>();
        this.reposWarning = new ArrayList<>();
        this.reposFailed = new ArrayList<>();
    }

    public void addRepoSuccess(String repoName) {
        this.reposSuccess.add(repoName);
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

    public int getNrOfReposWarning() {
        return this.reposWarning.size();
    }

    public int getNrOfReposFailed() {
        return this.reposFailed.size();
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

    public boolean hasWarnings() {
        return !this.reposWarning.isEmpty();
    }

    public int getNumberOfRepos() {
        return this.reposSuccess.size() + this.reposWarning.size() + this.reposFailed.size();
    }

}
