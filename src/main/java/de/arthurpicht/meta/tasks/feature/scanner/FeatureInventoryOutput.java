package de.arthurpicht.meta.tasks.feature.scanner;

import com.diogonunes.jcolor.Ansi;
import de.arthurpicht.meta.cli.output.Colors;
import de.arthurpicht.meta.config.RepoConfig;

import java.util.List;

public class FeatureInventoryOutput {

    public static void output(FeatureInventory featureInventory) {
        List<String> featureNames = featureInventory.getFeatureNames();
        for (String featureName : featureNames) {
            System.out.println(Ansi.colorize(featureName, Colors.whiteText));
            List<String> sortedRepoNames = featureInventory.getSortedRepoNames(featureName);
            for (String repoConfigName : sortedRepoNames) {
                System.out.println("    " + Ansi.colorize(repoConfigName, Colors.blueText));
            }
        }
    }

}
