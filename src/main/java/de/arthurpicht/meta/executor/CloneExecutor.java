package de.arthurpicht.meta.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.ExecutionContext;
import de.arthurpicht.meta.Meta;
import de.arthurpicht.meta.config.ConfigurationException;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.utils.core.strings.Strings;

public class CloneExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        if (cliCall.getOptionParserResultGlobal().hasOption(Meta.OPTION_META_DIR)) {
            ExecutionContext.init(cliCall.getOptionParserResultGlobal().getValue(Meta.OPTION_META_DIR));
        } else {
            ExecutionContext.init();
        }

        try {
            ProjectConfig projectConfig = new ProjectConfig(ExecutionContext.getMetaDir());

            System.out.println("Found projects: " + Strings.listing(projectConfig.getProjectNames(), " ", "", "", "[", "]"));

        } catch (ConfigurationException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }

    }

}
