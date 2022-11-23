package de.arthurpicht.meta.tasks.feature.scanner;

import com.diogonunes.jcolor.Ansi;
import de.arthurpicht.meta.cli.output.Colors;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;

import java.util.List;

public class FeatureInventoryOutput {

    public static void output(FeatureInfo featureInfo) {
        FeatureInventory featureInventory = featureInfo.getFeatureInventory();
        List<String> featureNames = featureInventory.getFeatureNames();
        for (String featureName : featureNames) {
            String spacer = isCheckoutFeature(featureInfo, featureName) ? "* " : "  ";
            System.out.println(Ansi.colorize(spacer + featureName, Colors.whiteText));
            List<String> sortedRepoNames = featureInventory.getSortedRepoNames(featureName);
            for (String repoConfigName : sortedRepoNames) {
                System.out.println("    " + Ansi.colorize(repoConfigName, Colors.blueText));
            }
        }
    }

    private static boolean isCheckoutFeature(FeatureInfo featureInfo, String featureName) {
        if (!featureInfo.hasFeature()) return false;
        return featureInfo.getFeature().getName().equals(featureName);
    }

}
