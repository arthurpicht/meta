package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.definitions.FeatureCheckoutDef;
import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.cli.target.ProjectTarget;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;
import de.arthurpicht.meta.tasks.feature.branchChangeSet.*;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureInventory;

import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.assertGitInstalled;
import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.initMetaConfig;

public class FeatureCheckoutExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        assertGitInstalled();

        ExecutionContext.init(cliCall);
        MetaConfig metaConfig = initMetaConfig();
        Target target = ProjectTarget.obtainInitializedTarget(metaConfig.getGeneralConfig().getTargets());
        String destinationFeatureName = getCliParameterFeature(cliCall);
        boolean force = cliCall.getOptionParserResultSpecific().hasOption(FeatureCheckoutDef.OPTION_FORCE);

        FeatureInfo sourceFeatureInfo = FeatureInfo.createFromPersistence(metaConfig, target);
        FeatureInfo destinationFeatureInfo = getDestinationFeatureInfo(sourceFeatureInfo, destinationFeatureName);

        BranchChangeSet branchChangeSet = BranchChangeSetCreator.create(
                sourceFeatureInfo,
                destinationFeatureInfo,
                force
        );
        BranchChangeSetDebugOut.debugOut(branchChangeSet);
        BranchChangeSetChecker.check(branchChangeSet, force);
        BranchChangeSetExecutor.execute(branchChangeSet, sourceFeatureInfo, destinationFeatureInfo);

        destinationFeatureInfo.getFeature().save();

        System.out.println("Successfully checked out feature [" + destinationFeatureName + "].");
    }

    private String getCliParameterFeature(CliCall cliCall) {
        return cliCall.getCliResult().getParameterParserResult().getParameterList().get(0);
    }

    private FeatureInfo getDestinationFeatureInfo(FeatureInfo sourceFeatureInfo, String destinationFeatureName)
            throws CommandExecutorException {
        FeatureInventory featureInventory = sourceFeatureInfo.getFeatureInventory();

        if (!featureInventory.hasFeatureName(destinationFeatureName))
            throw new CommandExecutorException("No such feature with name [" + destinationFeatureName + "].");

        return FeatureInfo.createFromPreexisting(
                sourceFeatureInfo,
                Feature.createFeatureByName(destinationFeatureName));
    }

}
