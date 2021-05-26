package de.arthurpicht.meta.cli.output;

import com.diogonunes.jcolor.Ansi;

public class Output {

    public static void message(String projectName, String message) {
        System.out.println(blueTag(projectName) + message);
    }

    public static void ok(String projectName, String message) {
        System.out.println(greenTag("OK") + blueTag(projectName) + message);
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


}
