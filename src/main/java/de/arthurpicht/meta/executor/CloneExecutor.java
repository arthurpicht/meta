package de.arthurpicht.meta.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.ExecutionContext;
import de.arthurpicht.meta.Meta;
import de.arthurpicht.meta.config.ConfigurationException;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
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

            for (String project : projectConfig.getProjectNames()) {

                RepoConfig repoConfig = projectConfig.getProjectConfig(project);

                System.out.println("[" + project + "] clone and checkout ...");

                Files.createDirectories(repoConfig.getDestinationPath());

                String url = repoConfig.getGitRepoUrl();
                if (cicd && repoConfig.hasGitRepoUrlReadOnly()) url = repoConfig.getGitRepoUrlReadOnly();

                Git.clone(repoConfig.getDestinationPath(), url, repoConfig.getRepoName());

                if (repoConfig.hasAlteredBranch()) {
                    Git.checkout(repoConfig.getRepoPath(), repoConfig.getBranch());
                }
            }

        } catch (ConfigurationException | GitException | IOException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }

    }

}
