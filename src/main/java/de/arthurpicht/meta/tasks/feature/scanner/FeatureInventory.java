package de.arthurpicht.meta.tasks.feature.scanner;

import java.util.*;
import java.util.stream.Collectors;

public class FeatureInventory {

    private final Map<String, Set<String>> featureMap;

    public FeatureInventory() {
        this.featureMap = new HashMap<>();
    }

    public void add(List<String> featureNameList, String repoName) {
        for (String featureName : featureNameList) {
            add(featureName, repoName);
        }
    }

    public void add(String featureName, String repoName) {
        Set<String> repoNameSet;
        if (this.featureMap.containsKey(featureName)) {
            repoNameSet = this.featureMap.get(featureName);

        } else {
            repoNameSet = new HashSet<>();
            this.featureMap.put(featureName, repoNameSet);
        }
        repoNameSet.add(repoName);
    }

    public List<String> getFeatureNames() {
        return this.featureMap.keySet().stream().sorted().collect(Collectors.toList());
    }

    public List<String> getRepoNames(String featureName) {
        if (!this.featureMap.containsKey(featureName))
            throw new IllegalArgumentException("No such featureName: [" + featureName + "].");
        return this.featureMap.get(featureName).stream().sorted().collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return this.featureMap.isEmpty();
    }

    public boolean hasRepoFeature(String repoName, String featureName) {
        Set<String> repoNames = this.featureMap.get(featureName);
        return repoNames.contains(repoName);
    }

}
