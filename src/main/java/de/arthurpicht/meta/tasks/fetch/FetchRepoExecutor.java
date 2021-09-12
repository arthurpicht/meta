package de.arthurpicht.meta.tasks.fetch;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.executor.CommandExecutorCommons;
import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.tasks.RepoExecutor;
import de.arthurpicht.meta.tasks.TaskSummary;

import java.nio.file.Path;

import static de.arthurpicht.meta.cli.output.Output.message;

public class FetchRepoExecutor extends RepoExecutor {

    @Override
    public void execute(RepoConfig repoConfig, Target target, TaskSummary taskSummary) {

        String repoName = repoConfig.getRepoName();
        Path repoPath = repoConfig.getRepoPath();

        message(repoName, "Operation pending ...");

        try {
            Git.fetch(repoPath);
            String currentBranch = CommandExecutorCommons.getCurrentBranch(repoConfig);
            boolean hasCommitsAhead = Git.hasCommitsAhead(repoPath, currentBranch);
            Output.deleteLastLine();
            if (!hasCommitsAhead) {
                Output.ok(repoName,"Repo is up-to-date.");
            } else {
                Output.okMagenta(repoName, "Repo has commits ahead. Pull needed.");
            }

        } catch (GitException e) {
            Output.error(repoName,"Git fetch failed: " + e.getMessage());
            if (ExecutionContext.isStacktrace())
                e.printStackTrace();
            taskSummary.addRepoFailed(repoName);
        }
    }

    @Override
    public boolean showSummary() {
        return false;
    }

}
