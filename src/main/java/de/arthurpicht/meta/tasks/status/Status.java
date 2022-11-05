package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.Repos;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;

public class Status {

    public static void execute(MetaConfig metaConfig, Target target, FeatureInfo featureInfo) {
        System.out.println(getStatusHeaderString(target, featureInfo));
        Repos.executeForAll(metaConfig, target, new StatusRepoExecutor(featureInfo));
    }

    private static String getStatusHeaderString(Target target, FeatureInfo featureInfo) {
        String statusHeaderString = "Status of [" + ExecutionContext.getMetaDirAsPath() + "] target [" + target.getName() + "]";
        if (featureInfo.hasFeature()) {
            statusHeaderString += " feature [" + featureInfo.getFeature().getName() + "]:";
        } else {
            statusHeaderString += ":";
        }
        return statusHeaderString;
    }

}
