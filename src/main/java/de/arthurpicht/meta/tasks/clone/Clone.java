package de.arthurpicht.meta.tasks.clone;

import de.arthurpicht.meta.cli.persistence.project.MetaPathsFile;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.Repos;
import de.arthurpicht.meta.tasks.TaskSummary;

import java.io.IOException;

public class Clone {

    public static TaskSummary execute(MetaConfig metaConfig, Target target) throws IOException {
        System.out.println("Cloning for target [" + target.getName() + "] ...");
        TaskSummary taskSummary = Repos.executeForAll(metaConfig, target, new CloneRepoExecutor());
        new MetaPathsFile(metaConfig, target).write();
        return taskSummary;
    }

}
