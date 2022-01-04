package de.arthurpicht.meta.config.exceptions;

public class UnknownTargetException extends ConfigurationException {

    public UnknownTargetException(String projectName, String targetName) {
        super("Unknown target: [" + targetName + "] in project [" + projectName + "].");
    }

}
