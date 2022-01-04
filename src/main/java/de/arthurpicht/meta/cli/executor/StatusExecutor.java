package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.target.ProjectTarget;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.MetaConfigFactory;
import de.arthurpicht.meta.config.exceptions.ConfigurationException;
import de.arthurpicht.meta.tasks.status.Status;

public class StatusExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        CommandExecutorCommons.assertGitInstalled();

        ExecutionContext.init(cliCall);
        MetaConfig metaConfig = initMetaConfig();
        Target target = ProjectTarget.obtainInitializedTarget(metaConfig.getGeneralConfig().getTargets());

        Status.execute(metaConfig, target);
    }

    private MetaConfig initMetaConfig() throws CommandExecutorException {
        try {
            return MetaConfigFactory.create(ExecutionContext.getMetaDir());
        } catch (ConfigurationException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }
    }

}
