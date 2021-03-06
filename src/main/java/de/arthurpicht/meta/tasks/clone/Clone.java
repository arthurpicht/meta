package de.arthurpicht.meta.tasks.clone;

import com.diogonunes.jcolor.Ansi;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.output.Colors;
import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.helper.FilesHelper;
import de.arthurpicht.meta.tasks.TaskSummary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static de.arthurpicht.meta.cli.output.Output.*;

public class Clone {

    public static TaskSummary execute(ProjectConfig projectConfig, Target target) throws IOException {

        System.out.println("Cloning for target [" + target + "] ...");

        boolean isTargetProd = target.equals(Target.PROD);
        boolean isTargetDev = target.equals(Target.DEV);

        TaskSummary taskSummary = new TaskSummary();

        for (String project : projectConfig.getProjectNames()) {

            RepoConfig repoConfig = projectConfig.getProjectConfig(project);
            String repoName = repoConfig.getRepoName();

            if (isTargetDev && !repoConfig.hasTargetDev()) continue;
            if (isTargetProd && !repoConfig.hasTargetProd()) continue;

            message(project, "Operation pending ...");

            if (isRepoDirPreexisting(repoConfig)) {
                handlePreexistingRepoDir(project, repoConfig, target, taskSummary);
                continue;
            }

            Files.createDirectories(repoConfig.getDestinationPath());

            try {
                gitClone(repoConfig, isTargetProd, ExecutionContext.isVerbose());
            } catch (GitException e) {
                Output.error(project,"Git clone failed: " + e.getMessage());
                if (ExecutionContext.isStacktrace())
                    e.printStackTrace();
                taskSummary.addRepoFailed(repoName);
                continue;
            }

            try {
                checkoutAlteredBranch(repoConfig, ExecutionContext.isVerbose());
            } catch (GitException e) {
                Output.error(project,"Git checkout failed: " + e.getMessage());
                if (ExecutionContext.isStacktrace())
                    e.printStackTrace();
                taskSummary.addRepoFailed(repoName);
                continue;
            }

            taskSummary.addRepoSuccess(repoName);
            if (!ExecutionContext.isVerbose()) {
                Output.deleteLastLine();
            }

            Output.ok(project,"Repo cloned successfully.");
        }

        summaryOut(taskSummary);

        return taskSummary;
    }

    private static void handlePreexistingRepoDir(
            String project, RepoConfig repoConfig, Target projectTarget, TaskSummary taskSummary
    ) {

        String repoName = repoConfig.getRepoName();
        boolean verbose = ExecutionContext.isVerbose();

        if (isRepoDir(repoConfig)) {
            boolean isTargetProd = projectTarget.equals(Target.PROD);
            if (isIntendedGitRepo(repoConfig, isTargetProd) && isIntendedBranch(repoConfig)) {
                taskSummary.addRepoWarning(repoName);
                if (!verbose)
                    Output.deleteLastLine();
                warning(project, "Repo already existing in intended branch. Consider performing update. Skip operation.");
            } else if (isIntendedGitRepo(repoConfig, isTargetProd) && !isIntendedBranch(repoConfig)) {
                taskSummary.addRepoFailed(repoName);
                if (!verbose)
                    Output.deleteLastLine();
                error(project, "Repo already existing with a checked out branch " +
                        "[" + getCurrentBranch(repoConfig) + "] other than intended branch " +
                        "[" + getIntendedBranchForCheckedOutRepo(repoConfig) + "].");
            } else {
                taskSummary.addRepoFailed(repoName);
                if (!verbose)
                    Output.deleteLastLine();
                error(project, "Wrong repo in destination. Expected: [" + getConfiguredUrl(repoConfig, isTargetProd) + "]. " +
                        "Actual: [" + getRemoteUrl(repoConfig) + "].");
            }
        } else {
            taskSummary.addRepoFailed(repoName);
            if (!verbose)
                Output.deleteLastLine();
            error(project, "Repo destination directory already existing but contains no repo.");
        }
    }

    private static boolean isRepoDirPreexisting(RepoConfig repoConfig) throws IOException {
        Path repoDir = repoConfig.getDestinationPath().resolve(repoConfig.getRepoName());
        return FilesHelper.isDirectoryNonEmpty(repoDir);
    }

    private static boolean isRepoDir(RepoConfig repoConfig) {
        Path repoDir = repoConfig.getDestinationPath().resolve(repoConfig.getRepoName());
        return Git.isGitRepo(repoDir);
    }

    private static boolean isIntendedGitRepo(RepoConfig repoConfig, boolean isTargetProd) {
        Path repoDir = repoConfig.getDestinationPath().resolve(repoConfig.getRepoName());
        String intendedUrl = getConfiguredUrl(repoConfig, isTargetProd);
        String repoUrl;
        try {
            repoUrl = Git.getRemoteUrlForOriginFetch(repoDir);
        } catch (GitException e) {
            throw new RuntimeException("Unexpected Git-Exception: " + e.getMessage(), e);
        }
        return (intendedUrl.equals(repoUrl));
    }

    private static boolean isIntendedBranch(RepoConfig repoConfig) {
        Path repoDir = repoConfig.getDestinationPath().resolve(repoConfig.getRepoName());
        String branch = getIntendedBranchForCheckedOutRepo(repoConfig);
        try {
            String currentBranch = Git.getCurrentBranch(repoDir);
            return currentBranch.equals(branch);
        } catch (GitException e) {
            throw new RuntimeException("Unexpected Git-Exception: " + e.getMessage(), e);
        }
    }

    private static String getCurrentBranch(RepoConfig repoConfig) {
        Path repoDir = repoConfig.getDestinationPath().resolve(repoConfig.getRepoName());
        try {
            return Git.getCurrentBranch(repoDir);
        } catch (GitException e) {
            throw new RuntimeException("Unexpected Git-Exception: " + e.getMessage(), e);
        }
    }

    private static String getRemoteUrl(RepoConfig repoConfig) {
        Path repoDir = repoConfig.getDestinationPath().resolve(repoConfig.getRepoName());
        try {
            return Git.getRemoteUrlForOriginFetch(repoDir);
        } catch (GitException e) {
            throw new RuntimeException("Unexpected Git-Exception: " + e.getMessage(), e);
        }
    }

    private static void gitClone(RepoConfig repoConfig, boolean isTargetProd, boolean verbose) throws GitException {
        String url = getConfiguredUrl(repoConfig, isTargetProd);
        Git.clone(repoConfig.getDestinationPath(), url, repoConfig.getRepoName(), verbose);
    }

    private static String getConfiguredUrl(RepoConfig repoConfig, boolean isTargetProd) {
        String url = repoConfig.getGitRepoUrl();
        if (isTargetProd && repoConfig.hasGitRepoUrlReadOnly()) url = repoConfig.getGitRepoUrlReadOnly();
        return url;
    }

    private static void checkoutAlteredBranch(RepoConfig repoConfig, boolean verbose) throws GitException {
        if (repoConfig.hasAlteredBranch()) {
            Path repoPath = repoConfig.getRepoPath();
            String branch = repoConfig.getBranch();

            if (!Git.hasBranchOnRemoteOrigin(repoPath, branch))
                throw new GitException("Intended branch [" + branch + "] not existing on remotes/origin.");

            Git.checkout(repoConfig.getRepoPath(), repoConfig.getBranch(), verbose);
        }
    }

    private static String getDefaultBranch(RepoConfig repoConfig) {
        Path repoDir = repoConfig.getDestinationPath().resolve(repoConfig.getRepoName());
        try {
            return Git.getDefaultBranch(repoDir);
        } catch (GitException e) {
            throw new RuntimeException("Unexpected Git-Exception: " + e.getMessage(), e);
        }
    }

    public static String getIntendedBranchForCheckedOutRepo(RepoConfig repoConfig) {
        if (repoConfig.hasAlteredBranch()) {
            return repoConfig.getBranch();
        } else {
            return getDefaultBranch(repoConfig);
        }
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
