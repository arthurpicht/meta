package de.arthurpicht.meta.git;

import de.arthurpicht.meta.Const;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class GitHighLevel {

    public static List<String> getFeatureBranches(Path repoPath) throws GitException {
        List<String> featureBranchNameList = Git.getRemoteBranches(repoPath);
        return featureBranchNameList.stream()
                .filter(featureBranchName -> featureBranchName.startsWith(Const.FEATURE_BRANCH_PREFIX))
                .collect(Collectors.toList());
    }

    public static List<String> getFeatureBranchNameList(Path repoPath) throws GitException {
        List<String> featureBranchNameList = Git.getRemoteBranches(repoPath);
        return featureBranchNameList.stream()
                .filter(featureBranchName -> featureBranchName.startsWith(Const.FEATURE_BRANCH_PREFIX))
                .map(featureBranchName -> featureBranchName.substring(Const.FEATURE_BRANCH_PREFIX.length()))
                .collect(Collectors.toList());
    }

}
