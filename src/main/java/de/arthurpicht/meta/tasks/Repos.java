package de.arthurpicht.meta.tasks;

import com.diogonunes.jcolor.Ansi;
import de.arthurpicht.meta.cli.output.Colors;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.RepoConfig;

import java.util.List;

public class Repos {

    public static TaskSummary executeForAll(MetaConfig metaConfig, Target target, RepoExecutor repoExecutor) {

        TaskSummary taskSummary = new TaskSummary();

        List<RepoConfig> repoConfigList = metaConfig.getRepoConfigsForTarget(target);
        repoConfigList.forEach(
                repoConfig -> repoExecutor.execute(repoConfig, target, taskSummary)
        );

        if (repoExecutor.showSummary()) summaryOut(taskSummary);

        return taskSummary;
    }

    private static void summaryOut(TaskSummary taskSummary) {
        System.out.println();
        if (taskSummary.hasSuccess()) {
            System.out.println(Ansi.colorize("REPOS PROCESSED SUCCESSFULLY.", Colors.greenText));
        } else {
            System.out.println(Ansi.colorize("ERROR ON PROCESSING REPOS.", Colors.redText));
        }
        System.out.println(taskSummary.getNumberOfRepos() + " repos processed: "
                + taskSummary.getNrOfReposSuccess() + " ok, "
                + taskSummary.getNrOfReposWarning() + " with warnings, "
                + taskSummary.getNrOfReposFailed() + " failed.");
    }

}
