package de.arthurpicht.meta.cli.persistence.user;

import de.arthurpicht.utils.io.file.SingleValueFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MetaDirFile {

    private static final String metaDirFileName = "meta_dir";
    private final SingleValueFile metaDirFile;

    public MetaDirFile() {
        Path targetFile = PersistenceUserDir.asPath().resolve(metaDirFileName);
        this.metaDirFile = new SingleValueFile(targetFile);
    }

    public Path asPath() {
        return this.metaDirFile.getPath();
    }

    public boolean exists() {
        return this.metaDirFile.exists();
    }

    public String read() throws IOException {
        return this.metaDirFile.read();
    }

    public void write(Path metaDir) throws IOException {
        Path parentDir = this.metaDirFile.getPath().getParent();
        if (!Files.exists(parentDir)) Files.createDirectories(parentDir);
        this.metaDirFile.write(metaDir.normalize().toAbsolutePath().toString());
    }

}
