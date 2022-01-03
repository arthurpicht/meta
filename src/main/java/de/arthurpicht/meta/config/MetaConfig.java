package de.arthurpicht.meta.config;

import java.util.*;

public class MetaConfig {

    private final List<String> projectNames;
    private final GeneralConfig generalConfig;
    private final Map<String, RepoConfig> repoConfigMap;

    public MetaConfig(GeneralConfig generalConfig, Map<String, RepoConfig> repoConfigMap) {
        String[] projectNames = repoConfigMap.keySet().toArray(new String[0]);
        this.projectNames = List.of(projectNames);
        this.generalConfig = generalConfig;
        this.repoConfigMap = Collections.unmodifiableMap(new LinkedHashMap<>(repoConfigMap));
    }

    public List<String> getProjectNames() {
        return this.projectNames;
    }

    public GeneralConfig getGeneralConfig() {
        return this.generalConfig;
    }

    public RepoConfig getProjectConfig(String projectName) {
        if (!this.repoConfigMap.containsKey(projectName))
            throw new IllegalArgumentException("Project not found: [" + projectName + "]");
        return this.repoConfigMap.get(projectName);
    }

}
