package de.arthurpicht.meta;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExecutionContext {

    private static final String ENV__PROJECT_META_DIR = "PROJECT_META_DIR";

    private static Path metaDir;
    private static boolean initialized = false;


    public static void init() {
        String projectMetaDir = System.getenv(ENV__PROJECT_META_DIR);
        if (projectMetaDir == null)
            throw new RuntimeException("Environment variable " + ENV__PROJECT_META_DIR + " not set.");

        metaDir = Paths.get(projectMetaDir);
        checkExistence();
        initialized = true;
    }

    public static void init(String metaDirCli) {
        metaDir = Paths.get(metaDirCli).normalize();
        checkExistence();
        initialized = true;
    }

    private static void checkExistence() {
        if (!Files.exists(metaDir))
            throw new RuntimeException("Value of " + ENV__PROJECT_META_DIR + " must reference an existing directory. Directory not found: [" + metaDir + "].");
        if (!Files.isDirectory(metaDir))
            throw new RuntimeException("Value of " + ENV__PROJECT_META_DIR + " must reference an existing directory. Not a directory: [" + metaDir + "].");
    }

    public static Path getMetaDir() {
        assertInitialized();
        return metaDir;
    }

    private static void assertInitialized() {
        if (!initialized) throw new IllegalStateException(ExecutionContext.class.getSimpleName() + " not initialized");
    }
}
