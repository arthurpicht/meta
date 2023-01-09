package de.arthurpicht.meta.tasks.feature;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;
import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNullAndNotEmpty;

public class FeatureBranchName {

    public final static String FEATURE_BRANCH_PREFIX = "feature-";
    private final String branchName;

    private FeatureBranchName(String branchName) {
        assertArgumentNotNull("branchName", branchName);
        if (!branchName.startsWith(FEATURE_BRANCH_PREFIX))
            throw new IllegalArgumentException("branch name does not start with [" + FEATURE_BRANCH_PREFIX + "].");
        if (branchName.length() == FEATURE_BRANCH_PREFIX.length())
            throw new IllegalArgumentException("Illegal branch name [" + branchName + "].");

        this.branchName = branchName;
    }

    public static FeatureBranchName createByBranchName(String branchName) {
        return new FeatureBranchName(branchName);
    }

    public static FeatureBranchName createByFeatureName(String featureName) {
        return new FeatureBranchName(FEATURE_BRANCH_PREFIX + featureName);
    }

    public String getBranchName() {
        return this.branchName;
    }

    public String getFeatureName() {
        return this.branchName.substring(FEATURE_BRANCH_PREFIX.length());
    }

    public static boolean isFeatureBranchName(String branchName) {
        return branchName.startsWith(FEATURE_BRANCH_PREFIX)
                && branchName.length() > FEATURE_BRANCH_PREFIX.length();
    }

    public static String getBranchName(String featureName) {
        assertArgumentNotNullAndNotEmpty("featureName", featureName);
        return FEATURE_BRANCH_PREFIX + featureName;
    }

}
