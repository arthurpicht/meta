package de.arthurpicht.meta.tasks;

import de.arthurpicht.meta.config.RepoConfig;

public abstract class RepoExecutor {

    public abstract void execute(RepoConfig repoConfig, TaskSummary taskSummary);
}
