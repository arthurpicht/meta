package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.Meta;
import de.arthurpicht.meta.config.ConfigurationException;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.tasks.TaskSummary;
import de.arthurpicht.meta.tasks.clone.Clone;
import de.arthurpicht.meta.tasks.clone.CloneConfig;
import de.arthurpicht.utils.core.strings.Strings;

import java.io.IOException;

public class CloneExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        ExecutionContext.init(cliCall);

        boolean cicd = cliCall.getOptionParserResultSpecific().hasOption(Meta.OPTION_CICD);

        try {
            ProjectConfig projectConfig = new ProjectConfig(ExecutionContext.getMetaDir());
            CloneConfig cloneConfig = CloneConfig.getInstance(projectConfig, cicd);

//            System.out.println("Found projects: "
//                    + Strings.listing(projectConfig.getProjectNames(), " ", "", "", "[", "]"));

            TaskSummary taskSummary = Clone.execute(cloneConfig);
            if (!taskSummary.hasSuccess()) throw new CommandExecutorException();

        } catch (ConfigurationException | IOException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }

    }

}
