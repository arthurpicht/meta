package de.arthurpicht.meta.cli.persistence.project;

import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class MetaPathsFile {

    private static final String RepoPathsFileName = "paths";

    private final List<String> pathStringList;
    private final Path metaDir;

    public MetaPathsFile(MetaConfig metaConfig, Target target) {
        this.pathStringList = new MetaPathsFileInitializer(metaConfig, target).getPathStringList();
        this.metaDir = metaConfig.getMetaDir();
    }

    public void write() throws IOException {
        if (!PersistenceProjectLocalDir.exists(this.metaDir)) {
            throw new RuntimeException("Meta project not initialized yet. Local persistence dir not found: [" + PersistenceProjectLocalDir.asPath(this.metaDir) + "].");
        }
        Path metaPathFile = PersistenceProjectLocalDir.asPath(this.metaDir).resolve(RepoPathsFileName);
        Files.deleteIfExists(metaPathFile);
        Files.write(metaPathFile, this.pathStringList, StandardOpenOption.CREATE_NEW);
    }

}
