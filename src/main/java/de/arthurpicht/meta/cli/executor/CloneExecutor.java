package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.config.ConfigurationException;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.tasks.TaskSummary;
import de.arthurpicht.meta.tasks.clone.Clone;
import de.arthurpicht.meta.tasks.clone.CloneConfig;
import de.arthurpicht.meta.tasks.clone.TargetFile;
import de.arthurpicht.meta.tasks.clone.TargetOption;

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

        TargetFile targetFile = new TargetFile(ExecutionContext.getMetaDir());
        TargetOption targetOption = new TargetOption(cliCall);

        if (targetFile.exists() && targetOption.isSpecified())
            throw new CommandExecutorException("Repos are initially cloned yet. " +
                    "Hence option --target has no power here.");

        if (targetFile.exists())
            return readFromTargetFile(targetFile);

        Target target;
        if (!targetOption.isSpecified()) {
            target = Target.DEV;
        } else {
            target = targetOption.getArgument();
        }

        writeToTargetFile(targetFile, target);

        return target;
    }

    private Target readFromTargetFile(TargetFile targetFile) throws CommandExecutorException {
        try {
            return targetFile.read();
        } catch (IOException e) {
            throw new CommandExecutorException("Error reading target file: [" + targetFile.getPath() + "]", e);
        }
    }

    private void writeToTargetFile(TargetFile targetFile, Target target) throws CommandExecutorException {
        try {
            targetFile.write(target);
        } catch (IOException e) {
            throw new CommandExecutorException("Error writing target file: [" + targetFile.getPath() + "]", e);
        }
    }

}
