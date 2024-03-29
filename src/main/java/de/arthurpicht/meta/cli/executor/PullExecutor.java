package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.target.ProjectTarget;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.pull.Pull;

import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.assertGitInstalled;
import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.initMetaConfig;

public class PullExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        assertGitInstalled();

        ExecutionContext.init(cliCall);
        MetaConfig metaConfig = initMetaConfig();
        Target target = ProjectTarget.obtainInitializedTarget(metaConfig.getGeneralConfig().getTargets());

        Pull.execute(metaConfig, target);
    }

}
