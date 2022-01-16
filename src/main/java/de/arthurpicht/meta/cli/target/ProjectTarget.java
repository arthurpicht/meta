package de.arthurpicht.meta.cli.target;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.persistence.project.TargetFile;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.utils.core.strings.Strings;

import java.io.IOException;

public class ProjectTarget {

    public static Target obtain(CliCall cliCall, Targets configuredTargets) {

        TargetFile targetFile = new TargetFile(ExecutionContext.getMetaDirAsPath());
        TargetOption targetOption = new TargetOption(cliCall);

        if (targetFile.exists() && targetOption.isSpecified())
            throw new MetaRuntimeException("Repos are initially cloned yet. " +
                    "Hence option --target has no power here.");

        if (targetFile.exists())
            return readFromTargetFile(targetFile, configuredTargets);

        return initializeBySpecifiedTarget(targetOption, configuredTargets, targetFile);
    }

    public static Target obtainInitializedTarget(Targets configuredTargets) {
        TargetFile targetFile = new TargetFile(ExecutionContext.getMetaDirAsPath());
        if (!targetFile.exists())
            throw new MetaRuntimeException("Target configuration not found. Are repos initially cloned yet?");
        return readFromTargetFile(targetFile, configuredTargets);
    }

    private static Target readFromTargetFile(TargetFile targetFile, Targets configuredTargets) {
        try {
            Target persistentTarget = targetFile.read();
            if (!configuredTargets.hasTarget(persistentTarget))
                throw new MetaRuntimeException("Target as initialized [" + persistentTarget.getName() + "] "
                        + "does not match one of configured targets ["
                        + Strings.listing(configuredTargets.getAllTargetNames(), ", ")
                        + "].");

            return persistentTarget;
        } catch (IOException e) {
            throw new MetaRuntimeException("Error reading target file: [" + targetFile.getPath() + "]", e);
        }
    }

    private static Target initializeBySpecifiedTarget(TargetOption targetOption, Targets configuredTargets, TargetFile targetFile) {
        if (!targetOption.isSpecified()) {
            throw new MetaRuntimeException("Repos not initially cloned yet. Please specify --target option.");
        }

        String specifiedTargetName = targetOption.getTargetArgument();
        if (!configuredTargets.hasTarget(specifiedTargetName))
            throw new MetaRuntimeException("Specified target [" + specifiedTargetName + "] "
                    + "does not match one of configured targets ["
                    + Strings.listing(configuredTargets.getAllTargetNames(), ", ")
                    + "].");

        Target target = new Target(specifiedTargetName);
        writeToTargetFile(targetFile, target);

        return target;
    }

    private static void writeToTargetFile(TargetFile targetFile, Target target) {
        try {
            targetFile.write(target);
        } catch (IOException e) {
            throw new MetaRuntimeException("Error writing target file: [" + targetFile.getPath() + "]", e);
        }
    }

}
