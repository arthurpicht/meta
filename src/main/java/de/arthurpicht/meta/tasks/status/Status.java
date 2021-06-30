package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.tasks.Target;

import java.nio.file.Path;

public class Status {

    public static void execute(ProjectConfig projectConfig, Target target) {

//        System.out.println("Found projects: "
//                + Strings.listing(projectConfig.getProjectNames(), " ", "", "", "[", "]"));

        for (String project : projectConfig.getProjectNames()) {

            RepoConfig repoConfig = projectConfig.getProjectConfig(project);
            String repoName = repoConfig.getRepoName();
            Path repoPath = repoConfig.getRepoPath();

            if (target == Target.DEV && !repoConfig.hasTargetDev()) continue;
            if (target == Target.PROD && !repoConfig.hasTargetProd()) continue;

            try {
                BranchStatus branchStatus = BranchStatusResolver.resolve(repoPath, repoName);
                BranchOutput.output(branchStatus);
            } catch (GitException e) {
                Output.error(repoName, "Unexpected error: " + e.getMessage());
            }
        }

    }

}
