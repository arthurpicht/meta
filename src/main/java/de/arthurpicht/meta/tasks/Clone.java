package de.arthurpicht.meta.tasks;

import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.helper.FilesHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Clone {

    public static void execute(ProjectConfig projectConfig, boolean isCicd, boolean verbose) throws IOException {

        TaskSummary taskSummary = new TaskSummary();

        for (String project : projectConfig.getProjectNames()) {

            RepoConfig repoConfig = projectConfig.getProjectConfig(project);

            System.out.println("[" + project + "] clone and checkout ...");

            if (isRepoPreexisting(repoConfig)) {
                taskSummary.addRepoWarning(repoConfig.getRepoName());
                continue;
            }

            Files.createDirectories(repoConfig.getDestinationPath());

            try {
                gitClone(repoConfig, isCicd, verbose);
            } catch (GitException e) {
                // TODO output error
                taskSummary.addRepoFailed(repoConfig.getRepoName());
                continue;
            }

            try {
                checkoutAlteredBranch(repoConfig);
            } catch (GitException e) {
                // TODO output error
                taskSummary.addRepoFailed(repoConfig.getRepoName());
                //noinspection UnnecessaryContinue
                continue;
            }

        }
    }

    private static boolean isRepoPreexisting(RepoConfig repoConfig) throws IOException {
        Path repoDir = repoConfig.getDestinationPath().resolve(repoConfig.getRepoName());
        return FilesHelper.isDirectoryNonEmpty(repoDir);
    }

    private static void gitClone(RepoConfig repoConfig, boolean isCicd, boolean verbose) throws GitException {
        String url = repoConfig.getGitRepoUrl();
        if (isCicd && repoConfig.hasGitRepoUrlReadOnly()) url = repoConfig.getGitRepoUrlReadOnly();
        Git.clone(repoConfig.getDestinationPath(), url, repoConfig.getRepoName(), verbose);
    }

    private static void checkoutAlteredBranch(RepoConfig repoConfig) throws GitException {
        if (repoConfig.hasAlteredBranch())
            Git.checkout(repoConfig.getRepoPath(), repoConfig.getBranch());
    }

}
