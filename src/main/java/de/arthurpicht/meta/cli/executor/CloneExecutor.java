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
import de.arthurpicht.meta.tasks.TaskSummary;
import de.arthurpicht.meta.tasks.clone.Clone;

import java.io.IOException;

public class CloneExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        CommandExecutorCommons.assertGitInstalled();

        ExecutionContext.init(cliCall);
        MetaConfig metaConfig = initMetaConfig();
        Target target = ProjectTarget.obtain(cliCall, metaConfig.getGeneralConfig().getTargets());

        TaskSummary taskSummary = execute(metaConfig, target);
        if (!taskSummary.hasSuccess()) throw new CommandExecutorException();
    }

    private MetaConfig initMetaConfig() throws CommandExecutorException {
        try {
            return MetaConfigFactory.create(ExecutionContext.getMetaDirAsPath());
        } catch (ConfigurationException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }
    }

    private TaskSummary execute(MetaConfig metaConfig, Target target) throws CommandExecutorException {
        try {
            return Clone.execute(metaConfig, target);
        } catch (IOException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }
    }

}
