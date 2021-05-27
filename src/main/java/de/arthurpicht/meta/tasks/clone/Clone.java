package de.arthurpicht.meta.tasks.clone;

import com.diogonunes.jcolor.Ansi;
import de.arthurpicht.meta.cli.output.Colors;
import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.helper.FilesHelper;
import de.arthurpicht.meta.tasks.TaskSummary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static de.arthurpicht.meta.cli.output.Output.message;
import static de.arthurpicht.meta.cli.output.Output.warning;

public class Clone {

    public static TaskSummary execute(CloneConfig cloneConfig) throws IOException {

        TaskSummary taskSummary = new TaskSummary();
        ProjectConfig projectConfig = cloneConfig.getProjectConfig();

        for (String project : projectConfig.getProjectNames()) {

            RepoConfig repoConfig = projectConfig.getProjectConfig(project);
            String repoName = repoConfig.getRepoName();

            if (cloneConfig.isTargetDev() && !repoConfig.hasTargetDev()) continue;
            if (cloneConfig.isTargetProd() && !repoConfig.hasTargetProd()) continue;

            message(project, "Operation pending ...");

            if (isRepoPreexisting(repoConfig)) {
                taskSummary.addRepoWarning(repoName);
                if (!cloneConfig.isVerbose())
                    Output.deleteLastLine();
                warning(project,"Repo already existing. Consider performing update. Skip operation.");
                continue;
            }

            Files.createDirectories(repoConfig.getDestinationPath());

            try {
                gitClone(repoConfig, cloneConfig.isTargetProd(), cloneConfig.isVerbose());
            } catch (GitException e) {
                Output.error(project,"Git clone failed: " + e.getMessage());
                if (cloneConfig.isStacktrace())
                    e.printStackTrace();
                taskSummary.addRepoFailed(repoName);
                continue;
            }

            try {
                checkoutAlteredBranch(repoConfig, cloneConfig.isVerbose());
            } catch (GitException e) {
                Output.error(project,"Git checkout failed: " + e.getMessage());
                if (cloneConfig.isStacktrace())
                    e.printStackTrace();
                // TODO check existence of targeted branch
                taskSummary.addRepoFailed(repoName);
                continue;
            }

            taskSummary.addRepoSuccess(repoName);
            if (!cloneConfig.isVerbose()) {
                Output.deleteLastLine();
            }

            Output.ok(project,"Repo cloned successfully.");
        }

        summaryOut(taskSummary);

        return taskSummary;
    }

    private static boolean isRepoPreexisting(RepoConfig repoConfig) throws IOException {
        Path repoDir = repoConfig.getDestinationPath().resolve(repoConfig.getRepoName());
        return FilesHelper.isDirectoryNonEmpty(repoDir);
    }

    private static void gitClone(RepoConfig repoConfig, boolean isTargetProd, boolean verbose) throws GitException {
        String url = repoConfig.getGitRepoUrl();
        if (isTargetProd && repoConfig.hasGitRepoUrlReadOnly()) url = repoConfig.getGitRepoUrlReadOnly();
        Git.clone(repoConfig.getDestinationPath(), url, repoConfig.getRepoName(), verbose);
    }

    private static void checkoutAlteredBranch(RepoConfig repoConfig, boolean verbose) throws GitException {
        if (repoConfig.hasAlteredBranch())
            Git.checkout(repoConfig.getRepoPath(), repoConfig.getBranch(), verbose);
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
