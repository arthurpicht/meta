package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProjectConfig {

    public static final String META_CONF_NAME = "meta.conf";
    private static final String SECTION_GENERAL = "general";

    private final Map<String, RepoConfig> projectConfigMap;

    public ProjectConfig(Path projectMetaDir) throws ConfigurationException {
        this.projectConfigMap = new HashMap<>();

        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        Path metaConfigFile = projectMetaDir.resolve(META_CONF_NAME);
        try {
            configurationFactory.addConfigurationFileFromFilesystem(metaConfigFile.toFile());
        } catch (ConfigurationFileNotFoundException | IOException e) {
            throw new ConfigurationException("meta file not found: [" + metaConfigFile.toAbsolutePath() + "].", e);
        }

        if (!configurationFactory.hasSection(SECTION_GENERAL))
            throw new ConfigurationException("Section [general] not found in meta config file [" + metaConfigFile + "]");
        Configuration generalSection = configurationFactory.getConfiguration(SECTION_GENERAL);
        GeneralConfig generalConfig = new GeneralConfig(generalSection, projectMetaDir);

        Set<String> sectionNames = configurationFactory.getSectionNames();
        sectionNames.remove(SECTION_GENERAL);
        for (String sectionName : sectionNames) {
            Configuration configuration = configurationFactory.getConfiguration(sectionName);
            RepoConfig repoConfig = new RepoConfig(configuration, generalConfig.getReferencePath());
            this.projectConfigMap.put(sectionName, repoConfig);
        }
    }

    public Set<String> getProjectNames() {
        return this.projectConfigMap.keySet();
    }

    public RepoConfig getProjectConfig(String projectName) {
        if (!this.projectConfigMap.containsKey(projectName))
            throw new IllegalArgumentException("Project not found: [" + projectName + "]");
        return this.projectConfigMap.get(projectName);
    }

}
