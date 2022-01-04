package de.arthurpicht.meta.tasks.pull;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.MetaConfigFactory;
import de.arthurpicht.meta.tasks.Repos;

public class Pull {

    public static void execute(MetaConfig metaConfig, Target target) {
        System.out.println("Executing pull on [" + ExecutionContext.getMetaDir() + "] target [" + target.getName() + "]:");
        Repos.executeForAll(metaConfig, target, new PullRepoExecutor());
    }

}
