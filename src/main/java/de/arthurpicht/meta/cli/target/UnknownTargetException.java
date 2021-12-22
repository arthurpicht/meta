package de.arthurpicht.meta.cli.target;

import de.arthurpicht.meta.exception.MetaException;

public class UnknownTargetException extends MetaException {

    private final String targetName;

    public UnknownTargetException(String projectName, String targetName) {
        super("Unknown target: [" + targetName + "] in project [" + projectName + "].");
        this.targetName = targetName;
    }

    public String getTargetName() {
        return this.targetName;
    }

}
