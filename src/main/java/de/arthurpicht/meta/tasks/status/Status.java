package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.git.GitException;

import java.nio.file.Path;

public class Status {

    public static void execute(ProjectConfig projectConfig) {

//        System.out.println("Found projects: "
//                + Strings.listing(projectConfig.getProjectNames(), " ", "", "", "[", "]"));

        for (String project : projectConfig.getProjectNames()) {

            RepoConfig repoConfig = projectConfig.getProjectConfig(project);
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

}
