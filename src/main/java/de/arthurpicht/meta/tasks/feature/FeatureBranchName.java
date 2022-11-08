package de.arthurpicht.meta.tasks.feature;

import de.arthurpicht.meta.Const;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;

public class FeatureBranchName {

    private final String branchName;

    private FeatureBranchName(String branchName) {
        assertArgumentNotNull("branchName", branchName);
        if (!branchName.startsWith(Const.FEATURE_BRANCH_PREFIX))
            throw new IllegalArgumentException("branch name does not start with [" + Const.FEATURE_BRANCH_PREFIX + "].");
        if (branchName.length() == Const.FEATURE_BRANCH_PREFIX.length())
            throw new IllegalArgumentException("Illegal branch name [" + branchName + "].");

        this.branchName = branchName;
    }

    public static FeatureBranchName createByBranchName(String branchName) {
        return new FeatureBranchName(branchName);
    }

    public static FeatureBranchName createByFeatureName(String featureName) {
        return new FeatureBranchName(Const.FEATURE_BRANCH_PREFIX + featureName);
    }

    public String getBranchName() {
        return this.branchName;
    }

    public String getFeatureName() {
        return this.branchName.substring(Const.FEATURE_BRANCH_PREFIX.length());
    }

    public static boolean isFeatureBranchName(String branchName) {
        return branchName.startsWith(Const.FEATURE_BRANCH_PREFIX)
                && branchName.length() > Const.FEATURE_BRANCH_PREFIX.length();
    }

}
