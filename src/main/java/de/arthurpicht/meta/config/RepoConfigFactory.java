package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.git.GitRepoUrl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepoConfigFactory {

    private static final String KEY_URL = "url";
    private static final String KEY_URL_RO = "urlReadOnly";
    private static final String KEY_DESTINATION_DIR = "destinationDir";
    private static final String KEY_REPO_NAME = "repoName";
    private static final String KEY_BRANCH = "branch";
    private static final String KEY_TARGET = "target";

    public static RepoConfig create(Configuration configuration, Path referencePath) throws ConfigurationException {
        String repoId = configuration.getSectionName();
        GitRepoUrl gitRepoUrl = createGitRepoUrl(configuration);
        Path destinationPath = obtainDestinationPath(configuration, referencePath);
        String repoName = obtainRepoName(configuration, gitRepoUrl);
        boolean isRepoNameAltered = obtainIsRepoNameAltered(configuration, repoName, gitRepoUrl);
        String branch = obtainBranch(configuration);
        Set<Target> targets = obtainTargets(configuration, repoId);

        return new RepoConfig(repoId, gitRepoUrl, destinationPath, repoName, isRepoNameAltered, branch, targets);
    }

    private static GitRepoUrl createGitRepoUrl(Configuration configuration) throws ConfigurationException {
        ConfigHelper.assertKey(configuration, KEY_URL);
        String urlString = configuration.getString(KEY_URL);

        if (configuration.containsKey(KEY_URL_RO)) {
            String urlRoString = configuration.getString(KEY_URL_RO);
            return new GitRepoUrl(urlString, urlRoString);
        } else {
            return new GitRepoUrl(urlString);
        }
    }

    private static Path obtainDestinationPath(Configuration configuration, Path referencePath) {
        Path destinationPath;
        if (configuration.containsKey(KEY_DESTINATION_DIR)) {
            Path destinationDirPath = Paths.get(configuration.getString(KEY_DESTINATION_DIR));
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
        if (configuration.containsKey(KEY_REPO_NAME)) {
            return configuration.getString(KEY_REPO_NAME);
        } else {
            return gitRepoUrl.getRepoName();
        }
    }

    private static boolean obtainIsRepoNameAltered(Configuration configuration, String repoName, GitRepoUrl gitRepoUrl) {
        return !repoName.equals(gitRepoUrl.getRepoName());
    }

    private static String obtainBranch(Configuration configuration) {
        if (configuration.containsKey(KEY_BRANCH)) {
            return configuration.getString(KEY_BRANCH);
        } else {
            return "";
        }
    }

    private static Set<Target> obtainTargets(Configuration configuration, String repoId) throws ConfigurationException {
        Set<Target> targets = new HashSet<>();
        if (configuration.containsKey(KEY_TARGET)) {
            List<String> targetStrings = configuration.getStringList(KEY_TARGET);
            for (String targetString : targetStrings) {
                if (targetString.equalsIgnoreCase(Target.DEV.name())) {
                    targets.add(Target.DEV);
                } else if (targetString.equalsIgnoreCase(Target.PROD.name())) {
                    targets.add(Target.PROD);
                } else {
                    throw new ConfigurationException("Illegal configuration value for repo [" + repoId + "] and key "
                            + "[" + KEY_TARGET + "]: '" + targetString + "'. Must be either '" + Target.DEV + "' or '"
                            + Target.PROD + "'.");
                }
            }
        } else {
            targets.add(Target.DEV);
            targets.add(Target.PROD);
        }
        return targets;
    }


}
