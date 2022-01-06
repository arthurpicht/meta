package de.arthurpicht.meta.cli;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.option.OptionParserResult;
import de.arthurpicht.meta.cli.MetaDir.MetaDir;
import de.arthurpicht.meta.cli.MetaDir.MetaDirBuilder;

import java.nio.file.Path;

public class ExecutionContext {

    private static MetaDir metaDir;
    private static boolean stacktrace;
    private static boolean verbose;
    private static boolean initialized = false;

    public static void init(CliCall cliCall) {
        OptionParserResult optionParserResultGlobal = cliCall.getOptionParserResultGlobal();
        metaDir = MetaDirBuilder.obtain(optionParserResultGlobal);
        verbose = optionParserResultGlobal.hasOption(Meta.OPTION_VERBOSE);
        stacktrace = optionParserResultGlobal.hasOption(Meta.OPTION_STACKTRACE);
        initialized = true;
    }

    private static void assertInitialized() {
        if (!initialized) throw new IllegalStateException(ExecutionContext.class.getSimpleName() + " not initialized");
    }

    public static Path getMetaDirAsPath() {
        assertInitialized();
        return metaDir.asPath();
    }

    public static MetaDir getMetaDir() {
        assertInitialized();
        return metaDir;
    }

    public static boolean isVerbose() {
        assertInitialized();
        return verbose;
    }

    public static boolean isStacktrace() {
        assertInitialized();
        return stacktrace;
    }

}
