package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.config.ConfigurationException;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.cli.target.TargetFile;
import de.arthurpicht.meta.tasks.status.Status;

import java.io.IOException;

public class StatusExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        ExecutionContext.init(cliCall);
        Target target = obtainTarget();

        try {
            ProjectConfig projectConfig = new ProjectConfig(ExecutionContext.getMetaDir());
            Status.execute(projectConfig, target);
        } catch (ConfigurationException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }
    }

    private Target obtainTarget() throws CommandExecutorException {

        TargetFile targetFile = new TargetFile(ExecutionContext.getMetaDir());

        if (!targetFile.exists())
            throw new CommandExecutorException("Target configuration not found. Are repos initially cloned yet?");

        return readFromTargetFile(targetFile);
    }

    private Target readFromTargetFile(TargetFile targetFile) throws CommandExecutorException {
        try {
            return targetFile.read();
        } catch (IOException e) {
            throw new CommandExecutorException("Error reading target file: [" + targetFile.getPath() + "]", e);
        }
    }

}
