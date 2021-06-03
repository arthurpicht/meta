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

import java.io.IOException;

public class CloneExecutor implements CommandExecutor {

    public enum Target { DEV, PROD }

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        ExecutionContext.init(cliCall);
        Target target = obtainTarget(cliCall);

        try {
            ProjectConfig projectConfig = new ProjectConfig(ExecutionContext.getMetaDir());
            CloneConfig cloneConfig = CloneConfig.getInstance(projectConfig, target);

//            System.out.println("Found projects: "
//                    + Strings.listing(projectConfig.getProjectNames(), " ", "", "", "[", "]"));

            TaskSummary taskSummary = Clone.execute(cloneConfig);
            if (!taskSummary.hasSuccess()) throw new CommandExecutorException();

        } catch (ConfigurationException | IOException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }
    }

    private Target obtainTarget(CliCall cliCall) throws CommandExecutorException {
        if (cliCall.getOptionParserResultSpecific().hasOption(Meta.OPTION_CLONE_TARGET)) {
            String targetSpec = cliCall.getOptionParserResultSpecific().getValue(Meta.OPTION_CLONE_TARGET);
            if (targetSpec.equalsIgnoreCase(Target.DEV.name())) {
                return Target.DEV;
            } else if (targetSpec.equalsIgnoreCase(Target.PROD.name())) {
                return Target.PROD;
            } else {
                throw new CommandExecutorException("Illegal value for option [" + Meta.OPTION_CLONE_TARGET + "]: '" + targetSpec + "'." +
                        " Must be either <dev> (default) or <prod>.");
            }
        }
        return Target.DEV;
    }

}
