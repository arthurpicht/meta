package de.arthurpicht.meta.config;

import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.utils.core.collection.Maps;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MetaConfig {

    private final Path metaDir;
    private final List<String> repoNames;
    private final GeneralConfig generalConfig;
    private final Map<String, RepoConfig> repoConfigMap;

    public MetaConfig(Path metaDir, GeneralConfig generalConfig, Map<String, RepoConfig> repoConfigMap) {
        this.metaDir = metaDir;
        String[] repoNames = repoConfigMap.keySet().toArray(new String[0]);
        this.repoNames = List.of(repoNames);
        this.generalConfig = generalConfig;
        this.repoConfigMap = Maps.immutableMap(repoConfigMap);
    }

    public Path getMetaDir() {
        return this.metaDir;
    }

    public List<String> getRepoNames() {
        return this.repoNames;
    }

    public GeneralConfig getGeneralConfig() {
        return this.generalConfig;
    }

    public RepoConfig getRepoConfig(String repoName) {
        if (!this.repoConfigMap.containsKey(repoName))
            throw new IllegalArgumentException("Repo config not found: [" + repoName + "]");
        return this.repoConfigMap.get(repoName);
    }

    public List<RepoConfig> getRepoConfigs(List<String> repoNames) {
        return repoNames.stream()
                .map(this::getRepoConfig)
                .collect(Collectors.toList());
    }

    public List<RepoConfig> getRepoConfigsForTarget(Target target) {
        return this.repoNames.stream()
                .map(this.repoConfigMap::get)
                .filter(repoConfig -> repoConfig.hasTarget(target))
                .collect(Collectors.toUnmodifiableList());
    }

}
