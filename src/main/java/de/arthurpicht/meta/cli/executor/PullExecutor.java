package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.target.ProjectTarget;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.ConfigurationException;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.pull.Pull;

public class PullExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        CommandExecutorCommons.assertGitInstalled();
        ExecutionContext.init(cliCall);
        Target target = ProjectTarget.obtain();

        try {
            MetaConfig metaConfig = new MetaConfig(ExecutionContext.getMetaDir());
            Pull.execute(metaConfig, target);
        } catch (ConfigurationException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }
    }

}
