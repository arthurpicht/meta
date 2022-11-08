package de.arthurpicht.meta.cli.feature;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.persistence.project.FeatureFile;
import de.arthurpicht.utils.core.assertion.MethodPreconditions;
import de.arthurpicht.utils.core.strings.Strings;

import java.io.IOException;

public class Feature {

    private final String name;

    private Feature(String featureName) {
        this.name = featureName;
    }

    public static Feature createFeatureByName(String name) {
        MethodPreconditions.assertArgumentNotNullAndNotEmpty("name", name);
        return new Feature(name);
    }

    public static Feature createWithNoFeature() {
        return new Feature("");
    }

    public static Feature load() {
        FeatureFile featureFile = new FeatureFile(ExecutionContext.getMetaDirAsPath());
        if (featureFile.exists()) {
            try {
                String featureName = featureFile.read();
                return createFeatureByName(featureName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return createWithNoFeature();
    }

    public boolean hasFeature() {
        return Strings.isSpecified(this.name);
    }

    public String getName() {
        if (!Strings.isSpecified(this.name))
            throw new IllegalStateException("No feature specified.");
        return this.name;
    }

}
