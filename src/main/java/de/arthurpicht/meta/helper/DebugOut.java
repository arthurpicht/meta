package de.arthurpicht.meta.helper;

import de.arthurpicht.meta.cli.ExecutionContext;

public class DebugOut {

    public static void println(String message) {
        if (ExecutionContext.isDebug()) System.out.println(message);
    }

    public static void print(String message) {
        if (ExecutionContext.isDebug()) System.out.print(message);
    }

}
