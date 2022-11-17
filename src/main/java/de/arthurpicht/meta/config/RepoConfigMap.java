package de.arthurpicht.meta.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RepoConfigMap {

    public static class Builder {

        private final Map<String, RepoConfig> map;

        public Builder() {
            this.map = new LinkedHashMap<>();
        }

        public Builder put(String repoId, RepoConfig repoConfig) {
            this.map.put(repoId, repoConfig);
            return this;
        }

        public RepoConfigMap build() {
            return new RepoConfigMap(this.map);
        }
    }

    private final Map<String, RepoConfig> map;
    private final List<String> repoIds;

    public RepoConfigMap(Map<String, RepoConfig> map) {
        this.map = map;
        this.repoIds = List.copyOf(this.map.keySet());
    }

    public boolean containsRepoId(String repoId) {
        return this.map.containsKey(repoId);
    }

    public RepoConfig getRepoConfig(String repoId) {
        return this.map.get(repoId);
    }

    public List<String> getRepIds() {
        return this.repoIds;
    }

}
