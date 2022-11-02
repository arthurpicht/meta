package de.arthurpicht.meta.cli.feature;

import de.arthurpicht.utils.core.assertion.MethodPreconditions;
import de.arthurpicht.utils.core.strings.Strings;

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

    public boolean hasFeature() {
        return this.name != null;
    }

    public String getName() {
        if (!Strings.isSpecified(this.name))
            throw new IllegalStateException("No feature specified.");
        return this.name;
    }

}
