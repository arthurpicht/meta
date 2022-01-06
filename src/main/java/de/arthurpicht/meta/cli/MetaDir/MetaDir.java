package de.arthurpicht.meta.cli.MetaDir;

import java.nio.file.Path;

public class MetaDir {

    private final Path metaDir;
    private final MetaDirSpecifier metaDirSpecifier;

    public MetaDir(Path metaDir, MetaDirSpecifier metaDirSpecifier) {
        this.metaDir = metaDir;
        this.metaDirSpecifier = metaDirSpecifier;
    }

    public Path asPath() {
        return metaDir;
    }

    public MetaDirSpecifier getSpecifier() {
        return metaDirSpecifier;
    }

}
