package de.arthurpicht.meta.cli.persistence.user;

import de.arthurpicht.utils.io.nio2.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PersistenceUserDir {

    private static final String PERSISTENCE_USER_DIR = ".meta";

    public static Path asPath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome).resolve(PERSISTENCE_USER_DIR);
    }

    public static boolean exists() {
        Path path = asPath();
        return FileUtils.isExistingDirectory(path);
    }

}
