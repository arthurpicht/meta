package de.arthurpicht.meta.cli.output;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.AnsiFormat;
import de.arthurpicht.meta.tasks.feature.FeatureBranchName;

public class Output {

    public static void message(String projectName, String message) {
        System.out.println(blueTag(projectName) + message);
    }

    public static void ok(String projectName, String message) {
        System.out.println(greenTag("OK") + blueTag(projectName) + message);
    }

    public static void okMagenta(String projectName, String message) {
        System.out.println(greenTag("OK") + blueTag(projectName) + Ansi.colorize(message, Colors.magentaText));
    }

    public static void okGreen(String projectName, String message) {
        System.out.println(greenTag("OK") + blueTag(projectName) + Ansi.colorize(message, Colors.greenText));
    }

    public static void skip(String projectName, String message) {
        System.out.println(greenTag("SKIP") + blueTag(projectName) + message);
    }

    public static void warning(String projectName, String message) {
        System.out.println(yellowTag("WARN") + blueTag(projectName) + message);
    }

    public static void error(String projectName, String message) {
        System.out.println(redTag("ERROR") + blueTag(projectName) + message);
    }

    public static String blueTag(String tag) {
        return Ansi.colorize("[" + tag + "] ", Colors.blueText);
    }

    public static String greenTag(String tag) {
        return Ansi.colorize("[" + tag + "] ", Colors.greenText);
    }

    public static String yellowTag(String tag) {
        return Ansi.colorize("[" + tag + "] ", Colors.yellowText);
    }

    public static String redTag(String tag) {
        return Ansi.colorize("[" + tag + "] ", Colors.redText);
    }

    public static void deleteLastLine() {
        int count = 1;
        System.out.printf("\033[%dA",count); // Move up
        System.out.print("\033[2K"); // Erase line content
    }

    public static String bracedBranch(String branchName, AnsiFormat ansiFormat) {
        if (FeatureBranchName.isFeatureBranchName(branchName)) {
            FeatureBranchName featureBranchName = FeatureBranchName.createByBranchName(branchName);
            return Ansi.colorize("(" + FeatureBranchName.FEATURE_BRANCH_PREFIX, ansiFormat)
                    + Ansi.colorize(featureBranchName.getFeatureName(), Colors.whiteText)
                    + Ansi.colorize(") ", ansiFormat);
        } else {
            return Ansi.colorize("(" + branchName + ") ", ansiFormat);
        }
    }

}
