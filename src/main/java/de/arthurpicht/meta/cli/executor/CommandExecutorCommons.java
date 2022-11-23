package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.MetaConfigFactory;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.config.exceptions.ConfigurationException;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;

import java.nio.file.Path;

public class CommandExecutorCommons {

    public static void assertGitInstalled() throws CommandExecutorException {
        try {
            if (!Git.hasGit()) throw new CommandExecutorException("Git command not found.");
        } catch (GitException e) {
            throw new RuntimeException("Unexpected Git-Exception: " + e.getMessage(), e);
        }
    }

    public static MetaConfig initMetaConfig() throws CommandExecutorException {
        try {
            return MetaConfigFactory.create(ExecutionContext.getMetaDirAsPath());
        } catch (ConfigurationException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }
    }

    public static String getCurrentBranch(RepoConfig repoConfig) {
        Path repoDir = repoConfig.getDestinationPath().resolve(repoConfig.getRepoName());
        try {
            return Git.getCurrentBranch(repoDir);
        } catch (GitException e) {
            throw new RuntimeException("Unexpected Git-Exception: " + e.getMessage(), e);
        }
    }

}
