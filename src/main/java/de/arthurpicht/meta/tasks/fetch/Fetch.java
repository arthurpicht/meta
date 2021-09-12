package de.arthurpicht.meta.tasks.fetch;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.Repos;
import de.arthurpicht.meta.tasks.status.StatusRepoExecutor;

public class Fetch {

    public static void execute(MetaConfig metaConfig, Target target) {

        System.out.println("Executing fetch on [" + ExecutionContext.getMetaDir() + "] target [" + target + "]:");

        Repos.executeForAll(metaConfig, target, new FetchRepoExecutor());
    }

}
