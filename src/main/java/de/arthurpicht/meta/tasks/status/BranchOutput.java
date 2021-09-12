package de.arthurpicht.meta.tasks.status;

import com.diogonunes.jcolor.Ansi;
import de.arthurpicht.meta.cli.output.Colors;
import de.arthurpicht.meta.cli.output.Output;

public class BranchOutput {

    public static void output(BranchStatus branchStatus) {

        if (!branchStatus.isRepo()) {
            Output.error(branchStatus.getRepoName(), "No git repo.");
            return;
        }

        String output = Output.blueTag(branchStatus.getRepoName());

        if (branchStatus.isStatusGreen()) {
            output += Output.greenBrace(branchStatus.getCurrentBranchName());
        } else if (branchStatus.isStatusYellow()) {
            output += Output.yellowBrace(branchStatus.getCurrentBranchName());
        } else {
            output += Output.redBrace(branchStatus.getCurrentBranchName());
        }

        if (branchStatus.hasUncommitedChanges()) {
            output += Ansi.colorize("Uncommited changes. ", Colors.redText);
        }

        if (branchStatus.hasUnpushedCommits()) {
            output += Ansi.colorize("Unpushed commits. ", Colors.blueText);
        }

        if (branchStatus.hasStash()) {
            output += Ansi.colorize("Stashes. ", Colors.yellowText);
        }

        if (branchStatus.hasCommitsAhead()) {
            output += Ansi.colorize("Commits ahead. ", Colors.magentaText);
        }

        System.out.println(output);
    }

}
