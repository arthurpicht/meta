package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneralConfig {

    public static String SECTION_GENERAL = "general";

    private static final String KEY_REFERENCE = "referenceDir";

    private final Path referencePath;

    public GeneralConfig(Configuration configuration, Path metaPath) throws ConfigurationException {
        ConfigHelper.assertKey(configuration, SECTION_GENERAL, KEY_REFERENCE);

        Path referenceValue = Paths.get(configuration.getString(KEY_REFERENCE));
        if (referenceValue.isAbsolute()) {
            referencePath = referenceValue.normalize().toAbsolutePath();
        } else {
            referencePath = metaPath.resolve(referenceValue).normalize().toAbsolutePath();
        }

    }

    public Path getReferencePath() {
        return this.referencePath;
    }

}
