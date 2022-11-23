package de.arthurpicht.meta.git;

import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;
import de.arthurpicht.meta.tasks.status.RepoProperties;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Repos {

    public static List<RepoConfig> selectReposWithUncommittedChanges(List<RepoConfig> repoConfigs) {
        List<RepoConfig> reposWithUncommittedChanges = new ArrayList<>();
        for (RepoConfig repoConfig : repoConfigs) {
            if (hasUncommittedChanges(repoConfig.getRepoPath()))
                reposWithUncommittedChanges.add(repoConfig);
        }
        return reposWithUncommittedChanges;
    }

    public static List<RepoConfig> selectReposWithModifiedFiles(List<RepoConfig> repoConfigs) {
        List<RepoConfig> reposWithModifiedFiles = new ArrayList<>();
        for (RepoConfig repoConfig : repoConfigs) {
            if (hasModifiedFiles(repoConfig.getRepoPath()))
                reposWithModifiedFiles.add(repoConfig);
        }
        return reposWithModifiedFiles;
    }

    public static List<RepoConfig> selectReposNotOnBaseBranch(List<RepoConfig> repoConfigs, FeatureInfo featureInfo) {
        List<RepoConfig> repoConfigsNotOnBase = new ArrayList<>();
        for (RepoConfig repoConfig : repoConfigs) {
            RepoProperties repoProperties = new RepoProperties(repoConfig, featureInfo);
            if (!isOnBaseBranch(repoProperties))
                repoConfigsNotOnBase.add(repoConfig);
        }
        return repoConfigsNotOnBase;
    }

    private static boolean isNotOnIntendedBranch(RepoProperties repoProperties) {
        try {
            return !repoProperties.isOnIntendedBranch();
        } catch (GitException e) {
            throw new MetaRuntimeException(e.getMessage(), e);
        }
    }

    private static boolean isOnBaseBranch(RepoProperties repoProperties) {
        try {
            return repoProperties.isOnBaseBranch();
        } catch (GitException e) {
            throw new MetaRuntimeException(e.getMessage(), e);
        }
    }

    private static boolean hasUncommittedChanges(Path repoPath) {
        try {
            return Git.hasUncommittedChanges(repoPath);
        } catch (GitException e) {
            throw new MetaRuntimeException(e.getMessage(), e);
        }
    }

    private static boolean hasModifiedFiles(Path repoPath) {
        try {
            return Git.hasModifiedFiles(repoPath);
        } catch (GitException e) {
            throw new MetaRuntimeException(e.getMessage(), e);
        }
    }

    public static void reset(List<RepoConfig> repoConfigs, boolean verbose, MetaConfig metaConfig, Target target) {
        FeatureInfo featureInfo = FeatureInfo.createForNoFeature(metaConfig, target);
        for (RepoConfig repoConfig : repoConfigs) {
            RepoProperties repoProperties = new RepoProperties(repoConfig, featureInfo);
            checkoutBaseBranch(repoProperties, verbose);
        }
    }

    private static void checkoutBaseBranch(RepoProperties repoProperties, boolean verbose) {
        try {
            Git.checkout(repoProperties.getRepoPath(), repoProperties.getBaseBranchName(), verbose);
        } catch (GitException e) {
            throw new MetaRuntimeException(e.getMessage(), e);
        }
    }

}
