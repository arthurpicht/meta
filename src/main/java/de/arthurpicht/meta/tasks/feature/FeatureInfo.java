package de.arthurpicht.meta.tasks.feature;

import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureInventory;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureScanner;

import java.util.ArrayList;
import java.util.List;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;

public class FeatureInfo {

    private final Feature feature;
    private final FeatureInventory featureInventory;

    public static FeatureInfo createForNoFeature(MetaConfig metaConfig, Target target) {
        return new FeatureInfo(
                Feature.createWithNoFeature(),
                FeatureScanner.scan(metaConfig, target));
    }

    public static FeatureInfo createFromPersistence(MetaConfig metaConfig, Target target) {
        Feature feature = Feature.load();
        FeatureInventory featureInventory = FeatureScanner.scan(metaConfig, target);
        return new FeatureInfo(feature, featureInventory);
    }

    public static FeatureInfo createFromPreexisting(FeatureInfo featureInfo, Feature feature) {
        if (!featureInfo.getFeatureInventory().hasFeature(feature))
            throw new IllegalArgumentException("No valid feature: [" + feature + "]");
        return new FeatureInfo(feature, featureInfo.getFeatureInventory());
    }

    public FeatureInfo(Feature feature, FeatureInventory featureInventory) {
        assertArgumentNotNull("feature", feature);
        assertArgumentNotNull("featureInventory", featureInventory);
        this.feature = feature;
        this.featureInventory = featureInventory;
    }

    public boolean hasFeature() {
        return this.feature.hasFeature();
    }

    public Feature getFeature() {
        return feature;
    }

    public FeatureInventory getFeatureInventory() {
        return this.featureInventory;
    }

    public List<RepoConfig> getRelatedRepoConfigs() {
        if (!hasFeature()) return new ArrayList<>();
        return this.featureInventory.getRepoConfigs(this.feature.getName());
    }

}
