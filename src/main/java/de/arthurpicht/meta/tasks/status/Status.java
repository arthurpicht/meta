package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.Repos;
import de.arthurpicht.meta.cli.target.Target;

public class Status {

    public static void execute(MetaConfig metaConfig, Target target) {
        System.out.println("Status of [" + ExecutionContext.getMetaDir() + "] target [" + target.getName() + "]:");
        Repos.executeForAll(metaConfig, target, new StatusRepoExecutor());
    }

}
