package de.arthurpicht.meta.cli.persistence.project;

import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.utils.io.file.SingleValueFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FeatureFile {

    private static final String featureFileName = "feature";
    private final SingleValueFile featureFile;

    public FeatureFile(Path metaDir) {
        Path featureFile = metaDir.resolve(PersistenceProjectLocalDir.PERSISTENCE_LOCAL_DIR).resolve(featureFileName);
        this.featureFile = new SingleValueFile(featureFile);
    }

    public Path getPath() {
        return this.featureFile.getPath();
    }

    public boolean exists() {
        return this.featureFile.exists();
    }

    public String read() throws IOException {
        return this.featureFile.read();
    }

    public void write(Feature feature) throws IOException {
        if (!feature.hasFeature())
            throw new IllegalArgumentException("No feature to write.");
        Path parentDir = this.featureFile.getPath().getParent();
        if (!Files.exists(parentDir)) Files.createDirectories(parentDir);
        this.featureFile.write(feature.getName());
    }

}
