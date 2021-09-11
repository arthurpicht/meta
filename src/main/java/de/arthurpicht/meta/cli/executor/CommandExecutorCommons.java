package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;

public class CommandExecutorCommons {

    public static void assertGitInstalled() throws CommandExecutorException {
        try {
            if (!Git.hasGit()) throw new CommandExecutorException("Git command not found.");
        } catch (GitException e) {
            throw new RuntimeException("Unexpected Git-Exception: " + e.getMessage(), e);
        }
    }

}
