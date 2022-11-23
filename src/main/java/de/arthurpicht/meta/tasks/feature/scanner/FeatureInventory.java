package de.arthurpicht.meta.tasks.feature.scanner;

import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.config.RepoConfig;

import java.util.*;
import java.util.stream.Collectors;

public class FeatureInventory {

    public static class Builder {

        private final FeatureMap.Builder featureMapBuilder;

        public Builder() {
            this.featureMapBuilder = new FeatureMap.Builder();
        }

        public void add(List<String> featureNameList, RepoConfig repoConfig) {
            this.featureMapBuilder.add(featureNameList, repoConfig);
        }

        public FeatureInventory build() {
            return new FeatureInventory(this.featureMapBuilder.build());
        }

    }

    private final FeatureMap featureMap;

    public FeatureInventory(FeatureMap featureMap) {
        this.featureMap = featureMap;
    }

    public List<String> getFeatureNames() {
        return this.featureMap.getFeatureNames().stream().sorted().collect(Collectors.toList());
    }

    public boolean hasFeatureName(String featureName) {
        return this.featureMap.containsFeature(featureName);
    }

    public boolean hasFeature(Feature feature) {
        return this.featureMap.containsFeature(feature.getName());
    }

    public List<RepoConfig> getRepoConfigs(String featureName) {
        if (!this.featureMap.containsFeature(featureName))
            throw new IllegalArgumentException("No such featureName: [" + featureName + "].");
        return List.copyOf(featureMap.getRepoConfigs(featureName));
    }

    public List<String> getSortedRepoNames(String featureName) {
        List<RepoConfig> repoConfigs = getRepoConfigs(featureName);
        return repoConfigs.stream()
                .map(RepoConfig::getRepoName)
                .sorted()
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return this.featureMap.isEmpty();
    }

    public boolean hasRepoFeature(String repoName, String featureName) {
        Set<RepoConfig> repoConfigs = this.featureMap.getRepoConfigs(featureName);
        return repoConfigs.stream()
                .map(RepoConfig::getRepoName)
                .collect(Collectors.toList())
                .contains(repoName);
    }

}
