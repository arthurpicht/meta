package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import de.arthurpicht.meta.config.exceptions.ConfigurationException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MetaConfigFactory {

    public static final String META_FILE_NAME = "meta.conf";
    private static final String SECTION_GENERAL = "general";

    private final GeneralConfig generalConfig;
    private final Map<String, RepoConfig> repoConfigMap;

    public static MetaConfig create(Path metaDir) throws ConfigurationException {
        MetaConfigFactory metaConfigFactory = new MetaConfigFactory(metaDir);
        return new MetaConfig(metaConfigFactory.generalConfig, metaConfigFactory.repoConfigMap);
    }

    private MetaConfigFactory(Path metaDir) throws ConfigurationException {
        ConfigurationFactory configurationFactory = bindConfigurationFile(metaDir);
        this.generalConfig = obtainGeneralConfig(metaDir, configurationFactory);
        this.repoConfigMap = obtainRepoConfigs(configurationFactory, this.generalConfig);
    }

    private ConfigurationFactory bindConfigurationFile(Path metaDir) throws ConfigurationException {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        Path metaFile = getMetaFile(metaDir);
        try {
            configurationFactory.addConfigurationFileFromFilesystem(metaFile.toFile());
        } catch (ConfigurationFileNotFoundException | IOException e) {
            throw new ConfigurationException(META_FILE_NAME + " not found: [" + metaFile.toAbsolutePath() + "].", e);
        }
        return configurationFactory;
    }

    private GeneralConfig obtainGeneralConfig(Path metaDir, ConfigurationFactory configurationFactory) throws ConfigurationException {
        if (!configurationFactory.hasSection(SECTION_GENERAL))
            return new GeneralConfig(metaDir);
        Configuration generalSection = configurationFactory.getConfiguration(SECTION_GENERAL);
        return new GeneralConfig(generalSection, metaDir);
    }

    private Map<String, RepoConfig> obtainRepoConfigs(ConfigurationFactory configurationFactory, GeneralConfig generalConfig)
            throws ConfigurationException {
        Set<String> sectionNames = configurationFactory.getSectionNames();
        sectionNames.remove(SECTION_GENERAL);
        Map<String, RepoConfig> repoConfigMap = new LinkedHashMap<>();
        for (String sectionName : sectionNames) {
            Configuration configuration = configurationFactory.getConfiguration(sectionName);
            RepoConfig repoConfig = RepoConfigFactory.create(configuration, generalConfig);
            repoConfigMap.put(sectionName, repoConfig);
        }
        return repoConfigMap;
    }

    private Path getMetaFile(Path metaDir) {
        return metaDir.resolve(META_FILE_NAME);
    }

}
