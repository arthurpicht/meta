package de.arthurpicht.meta.tasks.feature.scanner;

import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.git.GitHighLevel;
import de.arthurpicht.utils.io.nio2.FileUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FeatureScanner {

    public static FeatureInventory scan(MetaConfig metaConfig, Target target) {
        FeatureInventory.Builder featureInventoryBuilder = new FeatureInventory.Builder();
        List<RepoConfig> repoConfigList = metaConfig.getRepoConfigsForTarget(target);
        repoConfigList.forEach(repoConfig -> scanRepo(repoConfig, featureInventoryBuilder));
        return featureInventoryBuilder.build();
    }

    private static void scanRepo(RepoConfig repoConfig, FeatureInventory.Builder featureInventoryBuilder) {
        List<String> featureNameList = getFeatureNames(repoConfig);
        featureInventoryBuilder.add(featureNameList, repoConfig);
    }

    private static List<String> getFeatureNames(RepoConfig repoConfig) {
        Path repoPath = repoConfig.getRepoPath();
        if (!FileUtils.isExistingDirectory(repoPath)) return new ArrayList<>();
        try {
            return GitHighLevel.getFeatureBranchNameList(repoConfig.getRepoPath(), false);
        } catch (GitException e) {
            throw new MetaRuntimeException("Could not determine remote branches.", e);
        }
    }

}
