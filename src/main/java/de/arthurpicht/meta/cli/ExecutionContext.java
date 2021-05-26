package de.arthurpicht.meta.cli;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.option.OptionParserResult;
import de.arthurpicht.utils.core.strings.Strings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExecutionContext {

    private static final String ENV__PROJECT_META_DIR = "PROJECT_META_DIR";

    private static Path metaDir;
    private static boolean stacktrace;
    private static boolean verbose;

    private static boolean initialized = false;

    public static void init(CliCall cliCall) {
        OptionParserResult optionParserResultGlobal = cliCall.getOptionParserResultGlobal();

        obtainMetaDir(optionParserResultGlobal);

        verbose = optionParserResultGlobal.hasOption(Meta.OPTION_VERBOSE);
        stacktrace = optionParserResultGlobal.hasOption(Meta.OPTION_STACKTRACE);

        initialized = true;
    }

    private static void obtainMetaDir(OptionParserResult optionParserResultGlobal) {
        String metaDirSpec;
        if (optionParserResultGlobal.hasOption(Meta.OPTION_META_DIR)) {
            metaDirSpec = optionParserResultGlobal.getValue(Meta.OPTION_META_DIR);
        } else if (Strings.isSpecified(System.getenv(ENV__PROJECT_META_DIR))) {
            metaDirSpec = System.getenv(ENV__PROJECT_META_DIR);
        } else {
            throw new RuntimeException("Execution directory not specified. Either use --directory option or set "
                    + " environment variable '" + ENV__PROJECT_META_DIR + "'.");
        }
        metaDir = Paths.get(metaDirSpec).normalize();
        checkExistence();
    }

    private static void checkExistence() {
        if (!Files.exists(metaDir))
            throw new RuntimeException("Value of " + ENV__PROJECT_META_DIR + " must reference an existing directory. Directory not found: [" + metaDir + "].");
        if (!Files.isDirectory(metaDir))
            throw new RuntimeException("Value of " + ENV__PROJECT_META_DIR + " must reference an existing directory. Not a directory: [" + metaDir + "].");
    }

    private static void assertInitialized() {
        if (!initialized) throw new IllegalStateException(ExecutionContext.class.getSimpleName() + " not initialized");
    }

    public static Path getMetaDir() {
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
