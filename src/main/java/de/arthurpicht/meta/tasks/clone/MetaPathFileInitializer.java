package de.arthurpicht.meta.tasks.clone;

import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.helper.ListHelper;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class MetaPathFileInitializer {

    private final List<String> pathStringList;

    public MetaPathFileInitializer(MetaConfig metaConfig, Target target) {
        List<RepoConfig> repoConfigList = metaConfig.getRepoConfigsForTarget(target);

        this.pathStringList = obtainRepoDirs(repoConfigList);
        this.pathStringList.addAll(obtainParentDirs(repoConfigList));
        ListHelper.addIfNotYetContained(this.pathStringList, getReferencePathString(metaConfig));
        ListHelper.addIfNotYetContained(this.pathStringList, getMetaDirString(metaConfig));
        Collections.sort(this.pathStringList);
    }

    public List<String> getPathStringList() {
        return this.pathStringList;
    }

    private List<String> obtainRepoDirs(List<RepoConfig> repoConfigList) {
        return repoConfigList.stream()
                .map(RepoConfig::getRepoPath)
                .map(Path::toString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Set<String> obtainParentDirs(List<RepoConfig> repoConfigList) {
        return repoConfigList.stream()
                .map(RepoConfig::getRepoPath)
                .map(Path::getParent)
                .map(Path::toString)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private String getReferencePathString(MetaConfig metaConfig) {
        return metaConfig.getGeneralConfig().getReferencePath().toString();
    }

    private String getMetaDirString(MetaConfig metaConfig) {
        return metaConfig.getMetaDir().toString();
    }

}
