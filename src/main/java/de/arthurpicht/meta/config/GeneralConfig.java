package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.meta.cli.target.Targets;
import de.arthurpicht.meta.config.exceptions.ConfigurationException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GeneralConfig {

    public static final String SECTION_GENERAL = "general";

    private static final String KEY_REFERENCE_DIR = "referenceDir";
    private static final String KEY_TARGETS = "targets";

    private final Path referencePath;
    private final Targets targets;

    public GeneralConfig(Configuration configuration, Path metaPath) throws ConfigurationException {
        ConfigHelper.assertKey(configuration, KEY_REFERENCE_DIR);

        referencePath = obtainReferencePath(configuration, metaPath);
        targets = obtainTargets(configuration);
    }

    public Path getReferencePath() {
        return this.referencePath;
    }

    public Targets getTargets() {
        return this.targets;
    }

    private Path obtainReferencePath(Configuration configuration, Path metaPath) {
        Path referenceValue = Paths.get(configuration.getString(KEY_REFERENCE_DIR));
        if (referenceValue.isAbsolute()) {
            return referenceValue.toAbsolutePath().normalize();
        } else {
            return metaPath.resolve(referenceValue).toAbsolutePath().normalize();
        }
    }

    private Targets obtainTargets(Configuration configuration) throws ConfigurationException {
        List<String> targetNames = configuration.getStringList(KEY_TARGETS, "dev", "prod");
        Set<String> targetNameSet = new HashSet<>();
        for (String targetName : targetNames) {
            String targetNameNormalized = targetName.toLowerCase(Locale.ROOT);
            boolean distinct = targetNameSet.add(targetNameNormalized);
            if (!distinct) throw new ConfigurationException("Redundant definition in section [" + SECTION_GENERAL + "] " +
                    "parameter [" + KEY_TARGETS + "]: [" + targetName + "].");
        }
        return new Targets(targetNameSet);
    }

}
