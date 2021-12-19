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

    private static final String urlKey = "url";
    private static final String urlROKey = "urlReadOnly";
    private static final String destinationDirKey = "destinationDir";
    private static final String repoNameKey = "repoName";
    private static final String branchKey = "branch";
    private static final String targetKey = "target";

    public static RepoConfig create(Configuration configuration, Path referencePath) throws ConfigurationException {
        String repoId = configuration.getSectionName();
        GitRepoUrl gitRepoUrl = obtainGitRepoUrl(configuration);
        Path destinationPath = obtainDestinationPath(configuration, referencePath);
        String repoName = obtainRepoName(configuration, gitRepoUrl);
        boolean isRepoNameAltered = obtainIsRepoNameAltered(repoName, gitRepoUrl);
        String branch = obtainBranch(configuration);
        Set<Target> targets = obtainTargets(configuration, repoId);

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

    private static Set<Target> obtainTargets(Configuration configuration, String repoId) throws ConfigurationException {
        Set<Target> targets = new HashSet<>();
        if (configuration.containsKey(targetKey)) {
            List<String> targetStrings = configuration.getStringList(targetKey);
            for (String targetString : targetStrings) {
                if (targetString.equalsIgnoreCase(Target.DEV.name())) {
                    targets.add(Target.DEV);
                } else if (targetString.equalsIgnoreCase(Target.PROD.name())) {
                    targets.add(Target.PROD);
                } else {
                    throw new ConfigurationException("Illegal configuration value for repo [" + repoId + "] and key "
                            + "[" + targetKey + "]: '" + targetString + "'. Must be either '" + Target.DEV + "' or '"
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
