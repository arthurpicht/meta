package de.arthurpicht.meta.git;

import de.arthurpicht.meta.helper.InputStreamHelper;
import de.arthurpicht.meta.helper.StringHelper;
import de.arthurpicht.utils.core.collection.Lists;
import de.arthurpicht.utils.core.strings.Strings;
import de.arthurpicht.utils.io.nio2.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Git {

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

    public static boolean isGitRepo(Path repoPath) {
        // TODO: replace with "git rev-parse --show-toplevel" ...
        if (!FileUtils.isExistingDirectory(repoPath))
            throw new IllegalArgumentException("Assertion failed. No existing directory: [" + repoPath + "].");

        Path gitRepoDir = repoPath.resolve(".git");
        return FileUtils.isExistingDirectory(gitRepoDir);
    }

    public static boolean isUnderGitControl(Path repoPath) throws GitException {
        List<String> commands = List.of("git", "rev-parse", "--git-dir");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            process.waitFor();
            return (!result.isEmpty());
        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static String getRemoteUrlForOriginFetch(Path repoPath) throws GitException {

        List<String> commands = List.of("git", "remote", "-v");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0)
                throw new GitException("'git remote -v' exited with error code " + exitCode + ".");

            for (String resultString : result) {
                resultString = resultString.trim();
                if (resultString.startsWith("origin") && resultString.endsWith("(fetch)")) {
                    return StringHelper.getColumn(resultString, 1);
                }
            }

            throw new GitException("No remote string found for origin (fetch).");

        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static boolean hasBranchOnRemoteOrigin(Path repoPath, String branch) throws GitException {
        List<String> commands = List.of("git", "branch", "-avv");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0)
                throw new GitException("'git branch -avv' exited with error code " + exitCode + ".");

            for (String resultString : result) {
                resultString = resultString.trim();
                if (resultString.startsWith("remotes/origin/")) {
                    String remotesOriginBranch = StringHelper.getColumn(resultString, 0);
                    if (remotesOriginBranch.equals("remotes/origin/" + branch)) return true;
                }
            }

            return false;
        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static String getCurrentBranch(Path repoPath) throws GitException {
        List<String> commands = List.of("git", "branch");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0)
                throw new GitException("'git branch' exited with error code " + exitCode + ".");

            for (String resultString : result) {
                if (resultString.startsWith("*")) {
                    return resultString.substring(2).trim();
                }
            }

            throw new GitException("No current branch name found.");
        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static String getDefaultBranch(Path repoPath) throws GitException {
        // see: https://stackoverflow.com/questions/28666357/git-how-to-get-default-branch
        List<String> commands = List.of("git", "symbolic-ref", "refs/remotes/origin/HEAD");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                // remotes/origin/HEAD is not always defined. Error message is:
                // fatal: ref refs/remotes/origin/HEAD is not a symbolic ref
                // In that case output of 'git branch -avv' also
                // misses a line like '  remotes/origin/HEAD    -> origin/develop'.
                // This seems true especially for newly created EMPTY repos, created on (bitbucket) web.
                // After cloning symbolic ref HEAD is missing.
                // Calling 'git remote set-head origin --auto' fixes that.
                List<String> errorResult = InputStreamHelper.asStringList(process.getErrorStream());
                if (!errorResult.isEmpty() && errorResult.get(0).contains("is not a symbolic ref"))
                    throw new GitException("Could not determine default branch. Consider calling 'git remote set-head origin --auto' in repo.");

                throw new GitException("'git symbolic-ref refs/remotes/origin/HEAD' exited with error code " + exitCode + ".");
            }

            if (result.isEmpty())
                throw new GitException("No default branch found. No output for 'git symbolic-ref refs/remotes/origin/HEAD'.");

            String resultString = result.get(0);
            String refLeading = "refs/remotes/origin/";

            if (!resultString.startsWith(refLeading))
                throw new GitException("No default branch found. Result of 'git symbolic-ref refs/remotes/origin/HEAD' does not start with '" + refLeading + "'");

            return resultString.substring(refLeading.length());
        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static boolean hasUncommittedChanges(Path repoPath) throws GitException {
        List<String> commands = List.of("git", "status", "--porcelain");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0)
                throw new GitException("'git status --porcelain' exited with error code " + exitCode + ".");
            return (result.size() > 0);
        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static boolean hasUnpushedCommits(Path repoPath) throws GitException {
        List<String> commands = List.of("git", "log", "@{u}..");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                // ignore intentionally
                // will throw error 128 when executed on newly created branch before committed as upstream branch.
                return false;
            }
            return (result.size() > 0);
        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static boolean hasLocalChanges(Path repoPath) throws GitException {
        return hasUncommittedChanges(repoPath) || hasUncommittedChanges(repoPath);
    }

    public static boolean hasStash(Path repoPath) throws GitException {
        List<String> commands = List.of("git", "stash", "list");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0)
                throw new GitException("'git stash list' exited with error code " + exitCode + ".");
            return (result.size() > 0);
        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static void fetch(Path repoPath) throws GitException {
        List<String> commands = List.of("git", "fetch");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            InputStreamHelper.asStringList(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0)
                throw new GitException("'git fetch' exited with error code " + exitCode + ".");
        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static void pull(Path repoPath) throws GitException {
        List<String> commands = List.of("git", "pull");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            InputStreamHelper.asStringList(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0)
                throw new GitException("'git pull' exited with error code " + exitCode + ".");
        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static boolean hasCommitsAhead(Path repoPath, String branch) throws GitException {
        String remoteBranch = "origin/" + branch;
        List<String> commands = List.of("git", "log", remoteBranch, "^" + branch, "--oneline");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0)
                throw new GitException("'git log " + remoteBranch + " ^" + branch + " --oneline' exited with error code " + exitCode + ".");
            return (result.size() > 0);
        } catch (IOException | InterruptedException e) {
            throw new GitException(e);
        }
    }

    public static String getLastCommitId(Path repoPath) throws GitException {
        List<String> commands = List.of("git", "rev-parse", "HEAD");
        try {
            Process process = new ProcessBuilder().command(commands).directory(repoPath.toFile()).start();
            List<String> result = InputStreamHelper.asStringList(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0)
                throw new GitException("'git rev-parse HEAD' exited with error code " + exitCode + ".");
            if (result.size() != 1)
                throw new GitException("'git rev-parse HEAD' is expected to return exactly one line but is: " + result.size());
            return (result.get(0));
        } catch (IOException | InterruptedException e) {
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

}
