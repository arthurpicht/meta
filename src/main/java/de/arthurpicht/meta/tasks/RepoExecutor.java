package de.arthurpicht.meta.tasks;

import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.RepoConfig;

public abstract class RepoExecutor {

    public abstract void execute(RepoConfig repoConfig, Target target, TaskSummary taskSummary);

    public abstract boolean showSummary();

}
