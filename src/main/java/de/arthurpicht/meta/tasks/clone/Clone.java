package de.arthurpicht.meta.tasks.clone;

import de.arthurpicht.meta.cli.persistence.project.MetaPathsFile;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.Repos;
import de.arthurpicht.meta.tasks.TaskSummary;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;

import java.io.IOException;

public class Clone {

    public static TaskSummary execute(MetaConfig metaConfig, Target target, FeatureInfo featureInfo) throws IOException {
        System.out.println("Cloning for target [" + target.getName() + "] ...");
        TaskSummary taskSummary = Repos.executeForAll(metaConfig, target, new CloneRepoExecutor(featureInfo));
        new MetaPathsFile(metaConfig, target).write();
        return taskSummary;
    }

}
