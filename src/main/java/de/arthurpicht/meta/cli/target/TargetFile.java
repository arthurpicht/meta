package de.arthurpicht.meta.cli.target;

import de.arthurpicht.utils.io.file.SingleValueFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TargetFile {

    private final SingleValueFile targetFile;

    public TargetFile(Path metaDir) {
        Path targetFile = metaDir.resolve(".meta/local/target");
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
        return Target.valueOf(targetString);
    }

    public void write(Target target) throws IOException {
        Path parentDir = targetFile.getPath().getParent();
        if (!Files.exists(parentDir)) Files.createDirectories(parentDir);
        this.targetFile.write(target.toString());
    }

}
