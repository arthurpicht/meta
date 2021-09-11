package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.target.ProjectTarget;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.ConfigurationException;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.status.Status;

public class StatusExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        ExecutionContext.init(cliCall);
        Target target = ProjectTarget.obtain();

        try {
            MetaConfig metaConfig = new MetaConfig(ExecutionContext.getMetaDir());
            Status.execute(metaConfig, target);
        } catch (ConfigurationException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }
    }

}
