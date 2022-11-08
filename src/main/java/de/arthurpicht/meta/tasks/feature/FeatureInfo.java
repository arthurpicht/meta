package de.arthurpicht.meta.tasks.feature;

import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureInventory;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureScanner;

import java.util.List;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;

public class FeatureInfo {

    private final Feature feature;
    private final FeatureInventory featureInventory;

//    public static FeatureInfo createForNoFeature() {
//        return new FeatureInfo(Feature.createWithNoFeature(), null);
//    }

    public static FeatureInfo createFromPersistence(MetaConfig metaConfig, Target target) {
        Feature feature = Feature.load();
        if (feature.hasFeature()) {
            System.out.println("has Feature: " + feature.getName());
            FeatureInventory featureInventory = FeatureScanner.scan(metaConfig, target);
            return new FeatureInfo(feature, featureInventory);
        } else {
            System.out.println("no feature.");
            return new FeatureInfo(feature, null);
        }
    }

//    public static FeatureInfo createFromPreexisting(FeatureInfo featureInfo, String featureName) {
//        assertArgumentNotNull("featureInfo", featureInfo);
//        assertArgumentNotNull("fetureName", featureName);
//        if (featureInfo.hasFeature())
//    }


//    public static FeatureInfo create(Feature feature, FeatureInventory featureInventory) {
//        assertArgumentNotNull("feature", feature);
//        if (feature.hasFeature()) assertArgumentNotNull("featureInventory", feature);
//        return new FeatureInfo(feature, featureInventory);
//    }

    public FeatureInfo(Feature feature, FeatureInventory featureInventory) {
        assertArgumentNotNull("feature", feature);
        if (feature.hasFeature()) assertArgumentNotNull("featureInventory", featureInventory);
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
        assertFeature();
        return this.featureInventory;
    }

    public List<String> getRelatedRepos() {
        assertFeature();
        return this.featureInventory.getRepoNames(this.feature.getName());
    }

    private void assertFeature() {
        if (!hasFeature()) throw new IllegalStateException("No feature contained.");
    }

}
