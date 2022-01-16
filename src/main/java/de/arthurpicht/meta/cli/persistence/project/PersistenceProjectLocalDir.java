package de.arthurpicht.meta.cli.persistence.project;

import de.arthurpicht.meta.helper.FilesHelper;

import java.nio.file.Path;

public class PersistenceProjectLocalDir {

    public static final String PERSISTENCE_LOCAL_DIR = ".meta-local";

    public static Path asPath(Path metaDir) {
        return metaDir.resolve(PERSISTENCE_LOCAL_DIR);
    }

    public static boolean exists(Path metaDir) {
        Path localPersistenceDir = asPath(metaDir);
        return FilesHelper.isExistingDirectory(localPersistenceDir);
    }

}
