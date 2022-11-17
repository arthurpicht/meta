package de.arthurpicht.meta.config;

import de.arthurpicht.meta.cli.target.Target;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class MetaConfig {

    private final Path metaDir;
//    private final List<String> repoNames;
    private final GeneralConfig generalConfig;
//    private final Map<String, RepoConfig> repoConfigMap;
    private final RepoConfigMap repoConfigMap;

    public MetaConfig(Path metaDir, GeneralConfig generalConfig, RepoConfigMap repoConfigMap) {
        this.metaDir = metaDir;
//        String[] repoNames = repoConfigMap.keySet().toArray(new String[0]);
//        this.repoNames = List.of(repoNames);
        this.generalConfig = generalConfig;
        this.repoConfigMap = repoConfigMap;
    }

    public Path getMetaDir() {
        return this.metaDir;
    }

    public List<String> getIds() {
        return this.repoConfigMap.getRepIds();
    }

    public GeneralConfig getGeneralConfig() {
        return this.generalConfig;
    }

    public RepoConfig getRepoConfig(String repoId) {
        if (!this.repoConfigMap.containsRepoId(repoId))
            throw new IllegalArgumentException("Repo config not found for repoId: [" + repoId + "]");
        return this.repoConfigMap.getRepoConfig(repoId);
    }

    public List<RepoConfig> getRepoConfigs(List<String> repoIds) {
        return repoIds.stream()
                .map(this::getRepoConfig)
                .collect(Collectors.toList());
    }

    public List<RepoConfig> getRepoConfigsForTarget(Target target) {
        return this.repoConfigMap.getRepIds().stream()
                .map(this.repoConfigMap::getRepoConfig)
                .filter(repoConfig -> repoConfig.hasTarget(target))
                .collect(Collectors.toUnmodifiableList());
    }

}
