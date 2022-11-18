package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.target.ProjectTarget;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureInventoryOutput;

import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.assertGitInstalled;
import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.initMetaConfig;

public class FeatureShowExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        assertGitInstalled();

        ExecutionContext.init(cliCall);
        MetaConfig metaConfig = initMetaConfig();
        Target target = ProjectTarget.obtainInitializedTarget(metaConfig.getGeneralConfig().getTargets());
        FeatureInfo featureInfo = FeatureInfo.createFromPersistence(metaConfig, target);

        System.out.println("Features of [" + ExecutionContext.getMetaDirAsPath() + "] target [" + target.getName() + "]:");
        if (featureInfo.getFeatureInventory().isEmpty()) {
            System.out.println("No feature-branches found.");
        } else {
            FeatureInventoryOutput.output(featureInfo);
        }
    }

}
