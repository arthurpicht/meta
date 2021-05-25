package de.arthurpicht.meta.cli.output;

import com.diogonunes.jcolor.Ansi;

public class Output {

    public static void message(String projectName, String message) {
        System.out.println(blueTag(projectName) + message);
    }

    public static void ok(String message) {
        System.out.println(greenTag("OK") + message);
    }

    public static void warning(String message) {
        System.out.println(yellowTag("WARN") + message);
    }

    public static void error(String message) {
        System.out.println(redTag("ERROR") + message);
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

}
