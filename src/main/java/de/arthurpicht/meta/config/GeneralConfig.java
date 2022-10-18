package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.meta.cli.target.Targets;
import de.arthurpicht.meta.config.exceptions.ConfigurationException;
import de.arthurpicht.utils.core.collection.Sets;
import de.arthurpicht.utils.io.nio2.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GeneralConfig {

    public static final String SECTION_GENERAL = "general";
    public static final Set<String> TARGETS_DEFAULT = Sets.newHashSet("dev", "prod");

    private static final String KEY_REFERENCE_DIR = "referenceDir";
    private static final String KEY_TARGETS = "targets";

    private final Path referencePath;
    private final Targets targets;

    public GeneralConfig(Path metaPath) throws ConfigurationException {
        referencePath = getDefaultReferencePath(metaPath);
        targets = getDefaultTargets();
    }

    public GeneralConfig(Configuration configuration, Path metaPath) throws ConfigurationException {
        referencePath = obtainReferencePath(configuration, metaPath);
        targets = obtainTargets(configuration);
    }

    public Path getReferencePath() {
        return this.referencePath;
    }

    public Targets getTargets() {
        return this.targets;
    }

    private Path obtainReferencePath(Configuration configuration, Path metaPath) throws ConfigurationException {
        if (!configuration.containsKey(KEY_REFERENCE_DIR))
            return getDefaultReferencePath(metaPath);

        String referenceDirValue = configuration.getString(KEY_REFERENCE_DIR);
        Path referenceValue = Paths.get(referenceDirValue);

        if (referenceValue.isAbsolute()) {
            return referenceValue.toAbsolutePath().normalize();
        } else {
            return metaPath.resolve(referenceValue).toAbsolutePath().normalize();
        }
    }

    private Path getDefaultReferencePath(Path metaPath) throws ConfigurationException {
        if (FileUtils.isRootDirectory(metaPath))
            throw new ConfigurationException("Meta path [" + metaPath.toAbsolutePath() + "] is root directory.");
        return metaPath.resolve("..").toAbsolutePath().normalize();
    }

    private Targets obtainTargets(Configuration configuration) throws ConfigurationException {
        if (!configuration.containsKey(KEY_TARGETS))
            return getDefaultTargets();

        List<String> targetNames = configuration.getStringList(KEY_TARGETS);
        Set<String> targetNameSet = new HashSet<>();
        for (String targetName : targetNames) {
            String targetNameNormalized = targetName.toLowerCase(Locale.ROOT);
            boolean distinct = targetNameSet.add(targetNameNormalized);
            if (!distinct) throw new ConfigurationException("Redundant definition in section [" + SECTION_GENERAL + "] " +
                    "parameter [" + KEY_TARGETS + "]: [" + targetName + "].");
        }
        return new Targets(targetNameSet);
    }

    private Targets getDefaultTargets() {
        return new Targets(TARGETS_DEFAULT);
    }

}
