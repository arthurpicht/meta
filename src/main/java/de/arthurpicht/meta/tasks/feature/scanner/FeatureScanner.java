package de.arthurpicht.meta.tasks.feature.scanner;

import de.arthurpicht.meta.Const;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;

import java.util.List;
import java.util.stream.Collectors;

public class FeatureScanner {

    public static FeatureInventory scan(MetaConfig metaConfig, Target target) {
        FeatureInventory featureInventory = new FeatureInventory();
        List<RepoConfig> repoConfigList = metaConfig.getRepoConfigsForTarget(target);
        repoConfigList.forEach(repoConfig -> scanRepo(repoConfig, featureInventory));
        return featureInventory;
    }

    private static void scanRepo(RepoConfig repoConfig, FeatureInventory featureInventory) {
        List<String> branchNameList = getRemoteBranchNames(repoConfig);
        List<String> featureNameList = extractFeatureNames(branchNameList);
        featureInventory.add(featureNameList, repoConfig.getRepoName());
    }

//    private static List<String> getLocalBranchNames(RepoConfig repoConfig) {
//        try {
//            return Git.getLocalBranches(repoConfig.getRepoPath());
//        } catch (GitException e) {
//            throw new MetaRuntimeException("Could not determine local branches.", e);
//        }
//    }

    private static List<String> getRemoteBranchNames(RepoConfig repoConfig) {
        try {
            return Git.getRemoteBranches(repoConfig.getRepoPath());
        } catch (GitException e) {
            throw new MetaRuntimeException("Could not determine remote branches.", e);
        }
    }

    private static List<String> extractFeatureNames(List<String> localBranchNameList) {
        return localBranchNameList.stream()
                .filter(localBranchName -> localBranchName.startsWith(Const.FEATURE_BRANCH_PREFIX))
                .map(localBranchName -> localBranchName.substring(Const.FEATURE_BRANCH_PREFIX.length()))
                .collect(Collectors.toList());
    }

}
