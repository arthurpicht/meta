package de.arthurpicht.meta.config.exceptions;

public class RedundantTargetException extends ConfigurationException {

    public RedundantTargetException(String projectName, String targetName) {
        super("Redundant target: [" + targetName + "] in project [" + projectName + "].");
    }

}
