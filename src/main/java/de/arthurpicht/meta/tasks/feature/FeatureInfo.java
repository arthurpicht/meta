package de.arthurpicht.meta.tasks.feature;

import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureInventory;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureScanner;

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
            FeatureInventory featureInventory = FeatureScanner.scan(metaConfig, target);
            return new FeatureInfo(feature, featureInventory);
        } else {
            return new FeatureInfo(feature, null);
        }
    }

//    public static FeatureInfo create(Feature feature, FeatureInventory featureInventory) {
//        assertArgumentNotNull("feature", feature);
//        if (feature.hasFeature()) assertArgumentNotNull("featureInventory", feature);
//        return new FeatureInfo(feature, featureInventory);
//    }

    private FeatureInfo(Feature feature, FeatureInventory featureInventory) {
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
        return featureInventory;
    }

}
