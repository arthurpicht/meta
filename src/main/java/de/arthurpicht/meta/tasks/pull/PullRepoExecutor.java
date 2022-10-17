package de.arthurpicht.meta.tasks.pull;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.executor.CommandExecutorCommons;
import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.tasks.RepoExecutor;
import de.arthurpicht.meta.tasks.TaskSummary;
import de.arthurpicht.utils.io.nio2.FileUtils;

import java.nio.file.Path;

import static de.arthurpicht.meta.cli.output.Output.message;

public class PullRepoExecutor extends RepoExecutor {

    @Override
    public void execute(RepoConfig repoConfig, Target target, TaskSummary taskSummary) {

        String repoName = repoConfig.getRepoName();
        Path repoPath = repoConfig.getRepoPath();

        message(repoName, "Operation pending ...");

        try {
            if (!FileUtils.isExistingDirectory(repoPath))
                throw new MetaRuntimeException("Repo [" + repoPath.toAbsolutePath() + "] not found. Consider calling clone.");

            String message;
            if (Git.hasLocalChanges(repoPath)) {
                message = "Performed only fetch due to local changes. ";
                Git.fetch(repoPath);
                String currentBranch = CommandExecutorCommons.getCurrentBranch(repoConfig);
                boolean hasCommitsAhead = Git.hasCommitsAhead(repoPath, currentBranch);
                Output.deleteLastLine();
                if (!hasCommitsAhead) {
                    message += "Repo is up-to-date.";
                    Output.ok(repoName, message);
                } else {
                    message += "Repo has commits ahead. Needs manual merge.";
                    Output.ok(repoName, message);
                }
            } else {
                String lastCommitId = Git.getLastCommitId(repoPath);
                Git.pull(repoPath);
                String currentCommitId = Git.getLastCommitId(repoPath);
                Output.deleteLastLine();
                if (lastCommitId.equals(currentCommitId)) {
                    Output.ok(repoName, "Repo is up-to-date. No commits pulled.");
                } else {
                    Output.okGreen(repoName, "Repo is up-to-date. At least one commit pulled.");
                }
            }
        } catch (GitException e) {
            Output.deleteLastLine();
            Output.error(repoName, "Git fetch failed: " + e.getMessage());
            if (ExecutionContext.isStacktrace()) e.printStackTrace();
            taskSummary.addRepoFailed(repoName);
        } catch (MetaRuntimeException e) {
            Output.deleteLastLine();
            Output.error(repoName, e.getMessage());
            if (ExecutionContext.isStacktrace()) e.printStackTrace();
            taskSummary.addRepoFailed(repoName);
        }
    }

    @Override
    public boolean showSummary() {
        return false;
    }

}
