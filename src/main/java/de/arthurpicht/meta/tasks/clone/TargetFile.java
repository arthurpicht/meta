package de.arthurpicht.meta.tasks.clone;

import de.arthurpicht.meta.cli.executor.CloneExecutor;
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

    public CloneExecutor.Target read() throws IOException {
        String targetString = this.targetFile.read();
        return CloneExecutor.Target.valueOf(targetString);
    }

    public void write(CloneExecutor.Target target) throws IOException {
        Path parentDir = targetFile.getPath().getParent();
        if (!Files.exists(parentDir)) Files.createDirectories(parentDir);
        this.targetFile.write(target.toString());
    }

}
