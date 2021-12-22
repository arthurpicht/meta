package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import de.arthurpicht.meta.cli.target.RedundantTargetException;
import de.arthurpicht.meta.cli.target.UnknownTargetException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class MetaConfig {

    public static final String META_CONF_NAME = "meta.conf";
    private static final String SECTION_GENERAL = "general";

    private final GeneralConfig generalConfig;
    private final Map<String, RepoConfig> projectConfigMap;

    public MetaConfig(Path projectMetaDir) throws ConfigurationException, RedundantTargetException, UnknownTargetException {
        ConfigurationFactory configurationFactory = bindConfigurationFile(projectMetaDir);
        this.generalConfig = obtainGeneralConfig(projectMetaDir, configurationFactory);
        this.projectConfigMap = obtainProjectConfigs(configurationFactory, this.generalConfig);
    }

    public List<String> getProjectNames() {
        List<String> projectNames = new ArrayList<>(this.projectConfigMap.keySet());
        Collections.sort(projectNames);
        return projectNames;
    }

    public GeneralConfig getGeneralConfig() {
        return this.generalConfig;
    }

    public RepoConfig getProjectConfig(String projectName) {
        if (!this.projectConfigMap.containsKey(projectName))
            throw new IllegalArgumentException("Project not found: [" + projectName + "]");
        return this.projectConfigMap.get(projectName);
    }

    private ConfigurationFactory bindConfigurationFile(Path projectMetaDir) throws ConfigurationException {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        Path metaConfigFile = getMetaConfigFile(projectMetaDir);
        try {
            configurationFactory.addConfigurationFileFromFilesystem(metaConfigFile.toFile());
        } catch (ConfigurationFileNotFoundException | IOException e) {
            throw new ConfigurationException("meta file not found: [" + metaConfigFile.toAbsolutePath() + "].", e);
        }
        return configurationFactory;
    }

    private GeneralConfig obtainGeneralConfig(Path projectMetaDir, ConfigurationFactory configurationFactory) throws ConfigurationException {
        Path metaConfigFile = getMetaConfigFile(projectMetaDir);
        if (!configurationFactory.hasSection(SECTION_GENERAL))
            throw new ConfigurationException("Section [general] not found in meta config file [" + metaConfigFile + "]");
        Configuration generalSection = configurationFactory.getConfiguration(SECTION_GENERAL);
        return new GeneralConfig(generalSection, projectMetaDir);
    }

    private Map<String, RepoConfig> obtainProjectConfigs(ConfigurationFactory configurationFactory, GeneralConfig generalConfig)
            throws ConfigurationException, RedundantTargetException, UnknownTargetException {
        Set<String> sectionNames = configurationFactory.getSectionNames();
        sectionNames.remove(SECTION_GENERAL);
        Map<String, RepoConfig> projectConfigMap = new HashMap<>();
        for (String sectionName : sectionNames) {
            Configuration configuration = configurationFactory.getConfiguration(sectionName);
            RepoConfig repoConfig = RepoConfigFactory.create(configuration, generalConfig);
            projectConfigMap.put(sectionName, repoConfig);
        }
        return projectConfigMap;
    }

    private Path getMetaConfigFile(Path projectMetaDir) {
        return projectMetaDir.resolve(META_CONF_NAME);
    }

}
