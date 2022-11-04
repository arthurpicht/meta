package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.Repos;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureInventory;

public class Status {

    public static void execute(MetaConfig metaConfig, Target target, Feature feature) {
        System.out.println("Status of [" + ExecutionContext.getMetaDirAsPath() + "] target [" + target.getName() + "] feature [" + feature + "]:");
        Repos.executeForAll(metaConfig, target, new StatusRepoExecutor(feature));
    }

}
