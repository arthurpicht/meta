package de.arthurpicht.meta.git;

import de.arthurpicht.meta.helper.InputStreamHelper;
import de.arthurpicht.utils.core.collection.Lists;
import de.arthurpicht.utils.core.strings.Strings;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Git {

    // TODO invoke
    public static boolean hasGit() throws GitException {
        try {
            Process process = new ProcessBuilder("which", "git").start();
            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            if (process.waitFor() > 0) return false;
            return (result.size() > 0);
        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static void clone(Path destinationPath, String url, String repoName, boolean verbose) throws GitException {

        List<String> commands = Lists.newArrayList("git", "-C", destinationPath.toString(), "clone", url, repoName);

        if (verbose)
            System.out.println(Strings.listing(commands, " "));

        try {
            ProcessBuilder processBuilder = new ProcessBuilder()
                    .command(commands);
            if (verbose) processBuilder.inheritIO();
            Process process = processBuilder.start();

            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            List<String> errorResult = InputStreamHelper.asStringList(process.getErrorStream());

            outputResult(result, verbose);
            outputError(errorResult, verbose);

            int exitCode = process.waitFor();
            if (exitCode != 0)
                throw new GitException("'git clone' exited with with error code " + exitCode + ".");

        } catch (IOException | IllegalArgumentException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    private static void outputResult(List<String> result, boolean verbose) {
        if (!verbose) return;
        for (String string : result) {
            System.out.println(string);
        }
    }

    private static void outputError(List<String> errorResult, boolean verbose) {
        if (!verbose || errorResult.isEmpty()) return;
        for (String string : errorResult) {
            System.out.println(string);
        }
    }

    public static void checkout(Path repoPath, String branch, boolean verbose) throws GitException {

        List<String> commands = Lists.newArrayList("git", "-C", repoPath.toString(), "checkout", branch);
        if (verbose)
            System.out.println(Strings.listing(commands, " "));

        try {
            ProcessBuilder processBuilder = new ProcessBuilder().command(commands);
            if (verbose)
                processBuilder.inheritIO();
            Process process = processBuilder.start();

            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            List<String> errorResult = InputStreamHelper.asStringList(process.getErrorStream());

            outputResult(result, verbose);
            outputError(errorResult, verbose);

            int exitCode = process.waitFor();
            if (exitCode != 0)
                throw new GitException("'git checkout' exited with with error code " + exitCode + ".");

        } catch (IOException | IllegalArgumentException | InterruptedException e) {
            throw new GitException(e);
        }
    }

}
