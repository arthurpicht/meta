package de.arthurpicht.meta.cli.target;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.exception.MetaRuntimeException;

import java.io.IOException;

public class ProjectTarget {

    public static Target obtain(CliCall cliCall) {

        TargetFile targetFile = new TargetFile(ExecutionContext.getMetaDir());
        TargetOption targetOption = new TargetOption(cliCall);

        if (targetFile.exists() && targetOption.isSpecified())
            throw new MetaRuntimeException("Repos are initially cloned yet. " +
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

    private static Target readFromTargetFile(TargetFile targetFile) {
        try {
            return targetFile.read();
        } catch (IOException e) {
            throw new MetaRuntimeException("Error reading target file: [" + targetFile.getPath() + "]", e);
        }
    }

    private static void writeToTargetFile(TargetFile targetFile, Target target) {
        try {
            targetFile.write(target);
        } catch (IOException e) {
            throw new MetaRuntimeException("Error writing target file: [" + targetFile.getPath() + "]", e);
        }
    }

}
