package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.tasks.RepoExecutor;
import de.arthurpicht.meta.tasks.TaskSummary;

import java.nio.file.Path;

public class StatusRepoExecutor extends RepoExecutor {

    @Override
    public void execute(RepoConfig repoConfig, TaskSummary taskSummary) {
        String repoName = repoConfig.getRepoName();
        Path repoPath = repoConfig.getRepoPath();
        try {
            BranchStatus branchStatus = BranchStatusResolver.resolve(repoPath, repoName);
            BranchOutput.output(branchStatus);
        } catch (GitException e) {
            Output.error(repoName, "Unexpected error: " + e.getMessage());
        }
    }

}
