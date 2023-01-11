package de.arthurpicht.meta.cli.feature;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.persistence.project.FeatureFile;
import de.arthurpicht.meta.tasks.feature.FeatureBranchName;
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

    public static Feature load() {
        FeatureFile featureFile = new FeatureFile(ExecutionContext.getMetaDirAsPath());
        if (featureFile.exists()) {
            String featureName = featureFile.read();
            return createFeatureByName(featureName);
        }
        return createWithNoFeature();
    }

    public void save() {
        FeatureFile featureFile = new FeatureFile(ExecutionContext.getMetaDirAsPath());
        if (featureFile.exists()) featureFile.delete();
        featureFile.write(this);
    }

    public boolean hasFeature() {
        return Strings.isSpecified(this.name);
    }

    public String getName() {
        assertFeatureIsSpecified();
        return this.name;
    }

    public FeatureBranchName getFeatureBranchName() {
        assertFeatureIsSpecified();
        return FeatureBranchName.createByFeatureName(this.name);
    }

    @Override
    public String toString() {
        if (Strings.isSpecified(this.name)) return this.name;
        return "NO_FEATURE";
    }

    private void assertFeatureIsSpecified() {
        if (!Strings.isSpecified(this.name))
            throw new IllegalStateException("No feature specified.");
    }

}
