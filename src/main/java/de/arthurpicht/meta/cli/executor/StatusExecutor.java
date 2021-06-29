package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.config.ConfigurationException;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.tasks.status.Status;

public class StatusExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        ExecutionContext.init(cliCall);

        try {
            ProjectConfig projectConfig = new ProjectConfig(ExecutionContext.getMetaDir());
            Status.execute(projectConfig);
        } catch (ConfigurationException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }


    }

}
