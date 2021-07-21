package de.arthurpicht.meta.tasks;

import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.config.RepoConfig;

public class Repos {

    public static void executeForAll(ProjectConfig projectConfig, Target target, RepoExecutor repoExecutor) {

        for (String project : projectConfig.getProjectNames()) {

            RepoConfig repoConfig = projectConfig.getProjectConfig(project);

            if (target == Target.DEV && !repoConfig.hasTargetDev()) continue;
            if (target == Target.PROD && !repoConfig.hasTargetProd()) continue;

            TaskSummary taskSummary = new TaskSummary();

            repoExecutor.execute(repoConfig, taskSummary);
        }
    }

}
