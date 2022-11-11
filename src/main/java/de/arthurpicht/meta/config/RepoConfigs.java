package de.arthurpicht.meta.config;

import java.util.List;
import java.util.stream.Collectors;

public class RepoConfigs {

    public static List<String> getRepoNames(List<RepoConfig> repoConfigs) {
        return repoConfigs.stream().map(RepoConfig::getRepoName).collect(Collectors.toList());
    }

}
