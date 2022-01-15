package de.arthurpicht.meta.tasks.clone;

import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class MetaPathsFile {

    private static final String RepoPathsFileName = "meta-paths";

    private final List<String> pathStringList;
    private final Path metaDir;

    public MetaPathsFile(MetaConfig metaConfig, Target target) {
        this.pathStringList = new MetaPathFileInitializer(metaConfig, target).getPathStringList();
        this.metaDir = metaConfig.getMetaDir();
    }

    public void write() throws IOException {
        Path metaPathFile = this.metaDir.resolve(RepoPathsFileName);
        Files.deleteIfExists(metaPathFile);
        Files.write(metaPathFile, this.pathStringList, StandardOpenOption.CREATE_NEW);
    }

}
