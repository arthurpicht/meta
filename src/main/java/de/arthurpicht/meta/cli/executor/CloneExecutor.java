package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.target.ProjectTarget;
import de.arthurpicht.meta.config.ConfigurationException;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.tasks.TaskSummary;
import de.arthurpicht.meta.tasks.clone.Clone;

import java.io.IOException;

public class CloneExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        ExecutionContext.init(cliCall);
        Target target = ProjectTarget.obtain(cliCall);

        try {
            MetaConfig metaConfig = new MetaConfig(ExecutionContext.getMetaDir());

//            System.out.println("Found projects: "
//                    + Strings.listing(projectConfig.getProjectNames(), " ", "", "", "[", "]"));

            TaskSummary taskSummary = Clone.execute(metaConfig, target);
            if (!taskSummary.hasSuccess()) throw new CommandExecutorException();

        } catch (ConfigurationException | IOException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }
    }

}
