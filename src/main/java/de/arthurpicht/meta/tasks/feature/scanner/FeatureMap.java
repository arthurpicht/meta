package de.arthurpicht.meta.tasks.feature.scanner;

import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.utils.core.collection.Maps;

import java.util.*;

public class FeatureMap {

    public static class Builder {

        private final Map<String, Set<RepoConfig>> featureMap;

        public Builder() {
            this.featureMap = new HashMap<>();
        }

        public void add(List<String> featureNameList, RepoConfig repoConfig) {
            for (String featureName : featureNameList) {
                add(featureName, repoConfig);
            }
        }

        private void add(String featureName, RepoConfig repoConfig) {
            Set<RepoConfig> repoConfigs;
            if (this.featureMap.containsKey(featureName)) {
                repoConfigs = this.featureMap.get(featureName);
            } else {
                repoConfigs = new HashSet<>();
                this.featureMap.put(featureName, repoConfigs);
            }
            repoConfigs.add(repoConfig);
        }

        public FeatureMap build() {
            return new FeatureMap(this.featureMap);
        }

    }

    private final Map<String, Set<RepoConfig>> featureMap;

    public FeatureMap(Map<String, Set<RepoConfig>> featureMap) {
        this.featureMap = Maps.immutableMap(featureMap);
    }

    public Set<String> getFeatureNames() {
        return this.featureMap.keySet();
    }

    public boolean containsFeature(String featureName) {
        return this.featureMap.containsKey(featureName);
    }

    public Set<RepoConfig> getRepoConfigs(String featureName) {
        return this.featureMap.get(featureName);
    }

    public boolean isEmpty() {
        return this.featureMap.isEmpty();
    }

}
