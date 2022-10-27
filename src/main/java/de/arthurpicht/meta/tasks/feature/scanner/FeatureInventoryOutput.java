package de.arthurpicht.meta.tasks.feature.scanner;

import com.diogonunes.jcolor.Ansi;
import de.arthurpicht.meta.cli.output.Colors;

import java.util.List;

public class FeatureInventoryOutput {

    public static void output(FeatureInventory featureInventory) {
        List<String> featureNames = featureInventory.getFeatureNames();
        for (String featureName : featureNames) {
            System.out.println(Ansi.colorize(featureName, Colors.whiteText));
            List<String> repoNames = featureInventory.getRepoNames(featureName);
            for (String repoName : repoNames) {
                System.out.println("    " + Ansi.colorize(repoName, Colors.blueText));
            }
        }
    }

}
