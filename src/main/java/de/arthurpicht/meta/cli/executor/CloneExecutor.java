package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.Meta;
import de.arthurpicht.meta.config.ConfigurationException;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.tasks.Clone;
import de.arthurpicht.utils.core.strings.Strings;

import java.io.IOException;
import java.nio.file.Files;

public class CloneExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        if (cliCall.getOptionParserResultGlobal().hasOption(Meta.OPTION_META_DIR)) {
            ExecutionContext.init(cliCall.getOptionParserResultGlobal().getValue(Meta.OPTION_META_DIR));
        } else {
            ExecutionContext.init();
        }

        boolean cicd = cliCall.getOptionParserResultSpecific().hasOption(Meta.OPTION_CICD);

        try {
            ProjectConfig projectConfig = new ProjectConfig(ExecutionContext.getMetaDir());

            System.out.println("Found projects: " + Strings.listing(projectConfig.getProjectNames(), " ", "", "", "[", "]"));

            // TODO verbose flag from cli parameter
            // TODO process return flag (success/fail)
            Clone.execute(projectConfig, cicd, true);

        } catch (ConfigurationException | IOException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }

    }

}
