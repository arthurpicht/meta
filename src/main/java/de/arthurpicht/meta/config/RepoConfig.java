package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.meta.git.GitRepoUrl;
import de.arthurpicht.utils.core.strings.Strings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepoConfig {

    private enum Target {DEV, PROD}

    private static final String KEY_URL = "url";
    private static final String KEY_URL_RO = "urlReadOnly";
    private static final String KEY_DESTINATION_DIR = "destinationDir";
    private static final String KEY_REPO_NAME = "repoName";
    private static final String KEY_BRANCH = "branch";
    private static final String KEY_TARGET = "target";

    private final String repoId;
    private final GitRepoUrl gitRepoUrl;
    private final Path destinationPath;
    private final String repoName;
    private final boolean isRepoNameAltered;
    private final String branch;
    private final Set<Target> targets;

    public RepoConfig(Configuration configuration, Path referencePath) throws ConfigurationException {
        this.repoId = configuration.getSectionName();

        ConfigHelper.assertKey(configuration, this.repoId, KEY_URL);
        String urlString = configuration.getString(KEY_URL);
        if (configuration.containsKey(KEY_URL_RO)) {
            String urlRoString = configuration.getString(KEY_URL_RO);
            this.gitRepoUrl = new GitRepoUrl(urlString, urlRoString);
        } else {
            this.gitRepoUrl = new GitRepoUrl(urlString);
        }

        if (configuration.containsKey(KEY_DESTINATION_DIR)) {
            Path destinationDirPath = Paths.get(configuration.getString(KEY_DESTINATION_DIR));
            if (destinationDirPath.isAbsolute()) {
                this.destinationPath = destinationDirPath;
            } else {
                this.destinationPath = referencePath.resolve(destinationDirPath);
            }
            this.destinationPath.normalize().toAbsolutePath();
        } else {
            this.destinationPath = referencePath;
        }

        if (configuration.containsKey(KEY_REPO_NAME)) {
            this.repoName = configuration.getString(KEY_REPO_NAME);
        } else {
            this.repoName = this.gitRepoUrl.getRepoName();
        }

        this.isRepoNameAltered = !this.repoName.equals(this.gitRepoUrl.getRepoName());

        if (configuration.containsKey(KEY_BRANCH)) {
            this.branch = configuration.getString(KEY_BRANCH);
        } else {
            this.branch = "";
        }

        this.targets = new HashSet<>();
        if (configuration.containsKey(KEY_TARGET)) {
            List<String> targetStrings = configuration.getStringList(KEY_TARGET);
            for (String targetString : targetStrings) {
                if (targetString.equalsIgnoreCase(Target.DEV.name())) {
                    this.targets.add(Target.DEV);
                } else if (targetString.equalsIgnoreCase(Target.PROD.name())) {
                    this.targets.add(Target.PROD);
                } else {
                    throw new ConfigurationException("Illegal configuration value for repo [" + this.repoId + "] and key "
                            + "[" + KEY_TARGET + "]: '" + targetString + "'. Must be either '" + Target.DEV + "' or '"
                            + Target.PROD + "'.");
                }
            }
        } else {
            this.targets.add(Target.DEV);
            this.targets.add(Target.PROD);
        }
    }

    public String getRepoId() {
        return this.repoId;
    }

    public String getGitRepoUrl() {
        return this.gitRepoUrl.getUrl();
    }

    public boolean hasGitRepoUrlReadOnly() {
        return this.gitRepoUrl.hasUrlReadOnly();
    }

    public String getGitRepoUrlReadOnly() {
        return this.gitRepoUrl.getUrlReadOnly();
    }

    public Path getDestinationPath() {
        return this.destinationPath;
    }

    public boolean hasAlteredRepoName() {
        return this.isRepoNameAltered;
    }

    public String getRepoName() {
        return repoName;
    }

    public boolean hasAlteredBranch() {
        return Strings.isSpecified(this.branch);
    }

    public String getBranch() {
        return this.branch;
    }

    public Path getRepoPath() {
        return this.destinationPath.resolve(this.repoName);
    }

    public boolean hasTargetDev() {
        return this.targets.contains(Target.DEV);
    }

    public boolean hasTargetProd() {
        return this.targets.contains(Target.PROD);
    }

}
