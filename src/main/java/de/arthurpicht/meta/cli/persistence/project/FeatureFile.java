package de.arthurpicht.meta.cli.persistence.project;

import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.exception.MetaRuntimeException;
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

    public String read() {
        try {
            return this.featureFile.read();
        } catch (IOException e) {
            throw new MetaRuntimeException(e.getMessage(), e);
        }
    }

    public void write(Feature feature) {
        if (!feature.hasFeature())
            throw new IllegalArgumentException("No feature to write.");
        Path parentDir = this.featureFile.getPath().getParent();
        try {
            if (!Files.exists(parentDir)) Files.createDirectories(parentDir);
            this.featureFile.write(feature.getName());
        } catch (IOException e) {
            throw new MetaRuntimeException(e.getMessage(), e);
        }
    }

    public void delete() {
        try {
            this.featureFile.deleteIfExists();
        } catch (IOException e) {
            throw new MetaRuntimeException(e.getMessage(), e);
        }
    }

}
