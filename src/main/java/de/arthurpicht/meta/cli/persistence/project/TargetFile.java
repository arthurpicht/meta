package de.arthurpicht.meta.cli.persistence.project;

import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.utils.io.file.SingleValueFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TargetFile {

    private static final String targetFileName = "target";
    private final SingleValueFile targetFile;

    public TargetFile(Path metaDir) {
        Path targetFile = metaDir.resolve(PersistenceProjectLocalDir.PERSISTENCE_LOCAL_DIR).resolve(targetFileName);
        this.targetFile = new SingleValueFile(targetFile);
    }

    public Path getPath() {
        return this.targetFile.getPath();
    }

    public boolean exists() {
        return this.targetFile.exists();
    }

    public Target read() throws IOException {
        String targetString = this.targetFile.read();
        return new Target(targetString);
    }

    public void write(Target target) throws IOException {
        Path parentDir = this.targetFile.getPath().getParent();
        if (!Files.exists(parentDir)) Files.createDirectories(parentDir);
        this.targetFile.write(target.getName());
    }

}
