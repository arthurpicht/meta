package de.arthurpicht.meta.cli;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.option.OptionParserResult;
import de.arthurpicht.meta.cli.metaDir.MetaDir;
import de.arthurpicht.meta.cli.metaDir.MetaDirBuilder;

import java.nio.file.Path;

public class ExecutionContext {

    private static MetaDir metaDir;
    private static boolean stacktrace;
    private static boolean verbose;
    private static boolean initialized = false;

    public static void init(CliCall cliCall) {
        OptionParserResult optionParserResultGlobal = cliCall.getOptionParserResultGlobal();
        metaDir = MetaDirBuilder.obtain(optionParserResultGlobal);
        verbose = optionParserResultGlobal.hasOption(Meta.GLOBAL_OPTION__VERBOSE);
        stacktrace = optionParserResultGlobal.hasOption(Meta.GLOBAL_OPTION__STACKTRACE);
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
