package de.arthurpicht.meta.tasks.status;

import com.diogonunes.jcolor.Ansi;
import de.arthurpicht.meta.cli.output.Colors;
import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.git.GitException;

public class RepoStatusOutput {

    public static void output(RepoProperties repoProperties) throws GitException {

        if (!repoProperties.isRepoPathExisting()) {
            Output.error(
                    repoProperties.getRepoName(),
                    "Repo [" + repoProperties.getRepoPath().toAbsolutePath() + "] not found. " +
                            "Consider calling clone.");
            return;
        }

        if (!repoProperties.isRepo()) {
            Output.error(repoProperties.getRepoName(), "No git repo.");
            return;
        }

        String output = Output.blueTag(repoProperties.getRepoName());

        if (isStatusGreen(repoProperties)) {
            output += Output.bracedBranch(repoProperties.getCurrentBranchName(), Colors.greenText);
        } else if (isStatusYellow(repoProperties)) {
            output += Output.bracedBranch(repoProperties.getCurrentBranchName(), Colors.yellowText);
        } else {
            output += Output.bracedBranch(repoProperties.getCurrentBranchName(), Colors.redText);
        }

        if (repoProperties.hasUncommittedChanges()) {
            output += Ansi.colorize("Uncommitted changes. ", Colors.redText);
        }

        if (repoProperties.hasUnpushedCommits()) {
            output += Ansi.colorize("Unpushed commits. ", Colors.blueText);
        }

        if (repoProperties.hasStash()) {
            output += Ansi.colorize("Stashes. ", Colors.yellowText);
        }

        if (repoProperties.hasCommitsAhead()) {
            output += Ansi.colorize("Commits ahead. ", Colors.magentaText);
        }

        System.out.println(output);
    }

    private static boolean isStatusGreen(RepoProperties repoProperties) throws GitException {
        return repoProperties.isRepo()
                && !repoProperties.hasUncommittedChanges()
                && !repoProperties.hasUnpushedCommits()
                && !repoProperties.hasCommitsAhead()
                && !repoProperties.hasStash();
    }

    private static boolean isStatusYellow(RepoProperties repoProperties) throws GitException {
        return repoProperties.isRepo()
                && !repoProperties.hasUncommittedChanges()
                && !repoProperties.hasUnpushedCommits()
                && !repoProperties.hasCommitsAhead()
                && repoProperties.hasStash();
    }

}
