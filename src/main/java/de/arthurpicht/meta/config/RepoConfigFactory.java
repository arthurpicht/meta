package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.meta.cli.target.RedundantTargetException;
import de.arthurpicht.meta.cli.target.Targets;
import de.arthurpicht.meta.cli.target.UnknownTargetException;
import de.arthurpicht.meta.git.GitRepoUrl;
import de.arthurpicht.utils.core.collection.Lists;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class RepoConfigFactory {

    private static final String urlKey = "url";
    private static final String urlROKey = "urlReadOnly";
    private static final String destinationDirKey = "destinationDir";
    private static final String repoNameKey = "repoName";
    private static final String branchKey = "branch";
    private static final String targetKey = "target";

    public static RepoConfig create(Configuration configuration, GeneralConfig generalConfig)
            throws ConfigurationException, RedundantTargetException, UnknownTargetException {
        String repoId = configuration.getSectionName();
        GitRepoUrl gitRepoUrl = obtainGitRepoUrl(configuration);
        Path destinationPath = obtainDestinationPath(configuration, generalConfig.getReferencePath());
        String repoName = obtainRepoName(configuration, gitRepoUrl);
        boolean isRepoNameAltered = obtainIsRepoNameAltered(repoName, gitRepoUrl);
        String branch = obtainBranch(configuration);
        Targets targets = obtainTargets(configuration, generalConfig);

        return new RepoConfig(repoId, gitRepoUrl, destinationPath, repoName, isRepoNameAltered, branch, targets);
    }

    private static GitRepoUrl obtainGitRepoUrl(Configuration configuration) throws ConfigurationException {
        ConfigHelper.assertKey(configuration, urlKey);
        String urlString = configuration.getString(urlKey);

        if (configuration.containsKey(urlROKey)) {
            String urlRoString = configuration.getString(urlROKey);
            return new GitRepoUrl(urlString, urlRoString);
        } else {
            return new GitRepoUrl(urlString);
        }
    }

    private static Path obtainDestinationPath(Configuration configuration, Path referencePath) {
        Path destinationPath;
        if (configuration.containsKey(destinationDirKey)) {
            Path destinationDirPath = Paths.get(configuration.getString(destinationDirKey));
            if (destinationDirPath.isAbsolute()) {
                destinationPath = destinationDirPath;
            } else {
                destinationPath = referencePath.resolve(destinationDirPath);
            }
        } else {
            destinationPath = referencePath;
        }
        return destinationPath.normalize().toAbsolutePath();
    }

    private static String obtainRepoName(Configuration configuration, GitRepoUrl gitRepoUrl) {
        if (configuration.containsKey(repoNameKey)) {
            return configuration.getString(repoNameKey);
        } else {
            return gitRepoUrl.getRepoName();
        }
    }

    private static boolean obtainIsRepoNameAltered(String repoName, GitRepoUrl gitRepoUrl) {
        return !repoName.equals(gitRepoUrl.getRepoName());
    }

    private static String obtainBranch(Configuration configuration) {
        if (configuration.containsKey(branchKey)) {
            return configuration.getString(branchKey);
        } else {
            return "";
        }
    }

    private static Targets obtainTargets(Configuration repoConfiguration, GeneralConfig generalConfig)
            throws RedundantTargetException, UnknownTargetException {
        if (repoConfiguration.containsKey(targetKey)) {
            Set<String> targetStrings =
                    getDistinctTargetStrings(
                            repoConfiguration.getStringList(targetKey), repoConfiguration.getSectionName()
                    );
            checkSub(repoConfiguration.getSectionName(), generalConfig.getTargets().getAllTargetNames(), targetStrings);
            return new Targets(targetStrings);
        } else {
            return generalConfig.getTargets();
        }
    }

    private static Set<String> getDistinctTargetStrings(List<String> configuredTargetStrings, String projectName) throws RedundantTargetException {
        Set<String> targetStrings = new HashSet<>();
        for (String targetString : configuredTargetStrings) {
            targetString = targetString.toLowerCase();
            boolean distinct = targetStrings.add(targetString);
            if (!distinct) throw new RedundantTargetException(projectName, targetString);
        }
        return targetStrings;
    }

    private static void checkSub(String projectName, Set<String> configuredTargetNames, Set<String> repoTargetNames) throws UnknownTargetException {
        for (String repoTargetName : repoTargetNames) {
            if (!configuredTargetNames.contains(repoTargetName))
                throw new UnknownTargetException(projectName, repoTargetName);
        }
    }

}
