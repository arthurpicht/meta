package de.arthurpicht.meta.tasks;

import com.diogonunes.jcolor.Ansi;
import de.arthurpicht.meta.cli.output.Colors;
import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.helper.FilesHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static de.arthurpicht.meta.cli.output.Output.message;
import static de.arthurpicht.meta.cli.output.Output.warning;

public class Clone {

    public static TaskSummary execute(ProjectConfig projectConfig, boolean isCicd, boolean verbose) throws IOException {

        TaskSummary taskSummary = new TaskSummary();

        for (String project : projectConfig.getProjectNames()) {

            RepoConfig repoConfig = projectConfig.getProjectConfig(project);
            String repoName = repoConfig.getRepoName();

            message(project, "clone and checkout ...");

            if (isRepoPreexisting(repoConfig)) {
                taskSummary.addRepoWarning(repoName);
                warning(project,"Repo already existing. Consider performing update. Skip operation.");
                continue;
            }

            Files.createDirectories(repoConfig.getDestinationPath());

            try {
                gitClone(repoConfig, isCicd, verbose);
            } catch (GitException e) {
                Output.error(project,"Git clone failed: " + e.getMessage());
                // TODO output stacktrace
                taskSummary.addRepoFailed(repoName);
                continue;
            }

            try {
                checkoutAlteredBranch(repoConfig);
            } catch (GitException e) {
                Output.error(project,"Git checkout failed: " + e.getMessage());
                // TODO check existence of targeted branch
                // TODO output stacktrace
                taskSummary.addRepoFailed(repoName);
                continue;
            }

            taskSummary.addRepoSuccess(repoName);
            Output.ok(project,"Repo cloned successfully.");
        }

        summaryOut(taskSummary);

        return taskSummary;
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

    private static void summaryOut(TaskSummary taskSummary) {
        System.out.println();
        if (taskSummary.hasSuccess()) {
            System.out.println(Ansi.colorize("REPOS CLONED SUCCESSFULLY.", Colors.greenText));
        } else {
            System.out.println(Ansi.colorize("ERROR ON CLONING REPOS.", Colors.redText));
        }
        System.out.println(taskSummary.getNumberOfRepos() + " repos processed: "
                + taskSummary.getNrOfReposSuccess() + " ok, "
                + taskSummary.getNrOfReposWarning() + " with warnings, "
                + taskSummary.getNrOfReposFailed() + " failed.");
    }

}
