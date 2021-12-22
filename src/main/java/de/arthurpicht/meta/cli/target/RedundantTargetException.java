package de.arthurpicht.meta.cli.target;

import de.arthurpicht.meta.exception.MetaException;

public class RedundantTargetException extends MetaException {

    private final String targetName;

    public RedundantTargetException(String projectName, String targetName) {
        super("Redundant target: [" + targetName + "] in project [" + projectName + "].");
        this.targetName = targetName;
    }

}
