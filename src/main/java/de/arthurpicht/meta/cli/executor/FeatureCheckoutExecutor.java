package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.definitions.FeatureCheckoutDef;
import de.arthurpicht.meta.cli.definitions.FeatureResetDef;
import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.cli.feature.branchChangeSet.BranchChangeSet;
import de.arthurpicht.meta.cli.feature.branchChangeSet.BranchChangeSetDebugOut;
import de.arthurpicht.meta.cli.target.ProjectTarget;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.config.RepoConfigs;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.git.Repos;
import de.arthurpicht.meta.helper.DebugOut;
import de.arthurpicht.meta.helper.ListHelper;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureInventory;
import de.arthurpicht.meta.tasks.status.RepoProperties;
import de.arthurpicht.utils.core.strings.Strings;

import java.util.ArrayList;
import java.util.List;

import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.assertGitInstalled;
import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.initMetaConfig;

public class FeatureCheckoutExecutor implements CommandExecutor {


    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        assertGitInstalled();

        ExecutionContext.init(cliCall);
        MetaConfig metaConfig = initMetaConfig();
        Target target = ProjectTarget.obtainInitializedTarget(metaConfig.getGeneralConfig().getTargets());
        String destinationFeatureName = getCliParameterFeature(cliCall);
        boolean force = cliCall.getOptionParserResultSpecific().hasOption(FeatureCheckoutDef.OPTION_FORCE);

        FeatureInfo sourceFeatureInfo = FeatureInfo.createFromPersistence(metaConfig, target);
        FeatureInfo destinationFeatureInfo = getDestinationFeatureInfo(sourceFeatureInfo, destinationFeatureName);

        BranchChangeSet branchChangeSet = BranchChangeSet.create(
                sourceFeatureInfo,
                destinationFeatureInfo,
                force
        );

        BranchChangeSetDebugOut.debugOut(branchChangeSet);

        if (force && branchChangeSet.hasUncommittedChangesBlockingBranches()) {
            List<String> uncommittedChangedRepoNames
                    = RepoConfigs.getRepoNames(branchChangeSet.getUncommittedChangesBlockingBranches());
            System.out.println(
                    "The following repos are affected and have " +
                            "uncommitted changes: " + Strings.listing(uncommittedChangedRepoNames, ", ") + ".\n" +
                            "Force checkout.");
        }

        if (branchChangeSet.isBlocked()) {
            if (force) {
                List<String> modifiedFilesRepos
                        = RepoConfigs.getRepoNames(branchChangeSet.getModifiedFilesBlockingBranches());
                throw new CommandExecutorException("There are modified files in the following repos: "
                        + Strings.listing(modifiedFilesRepos, ", ") + "."
                );
            } else {
                List<String> uncommittedChangesRepos
                        = RepoConfigs.getRepoNames(branchChangeSet.getUncommittedChangesBlockingBranches());
                throw new CommandExecutorException(
                        "Cannot checkout feature. The following repos are affected and have " +
                                "uncommitted changes: " + Strings.listing(uncommittedChangesRepos, ", ") + ".\n" +
                                "Consider calling option --force.");
            }
        }

//        checkPreconditions(sourceFeatureInfo, destinationFeatureInfo, force);
//
//        checkoutBase(sourceFeatureInfo);
//        checkoutFeature(destinationFeatureInfo);

        checkoutBase(branchChangeSet, sourceFeatureInfo);
        checkoutFeature(branchChangeSet, destinationFeatureInfo);

        destinationFeatureInfo.getFeature().save();

        System.out.println("Successfully checked out feature [" + destinationFeatureName + "].");
    }

    private void checkPreconditions(FeatureInfo sourceFeatureInfo, FeatureInfo destinationFeatureInfo, boolean force)
            throws CommandExecutorException {

        List<RepoConfig> affectedRepos = determineAffectedRepos(sourceFeatureInfo, destinationFeatureInfo);
        List<RepoConfig> uncommittedChangedRepos = Repos.selectReposWithUncommittedChanges(affectedRepos);
        List<RepoConfig> toBeProcessedRepos
                = Repos.selectReposNotOnFeatureBranch(uncommittedChangedRepos, destinationFeatureInfo.getFeature());

        if (!toBeProcessedRepos.isEmpty()) {
            List<String> uncommittedChangedRepoNames = RepoConfigs.getRepoNames(toBeProcessedRepos);
            if (!force) {
                throw new CommandExecutorException(
                        "Cannot checkout feature. The following repos are affected and have " +
                                "uncommitted changes: " + Strings.listing(uncommittedChangedRepoNames, ", ") + ".\n" +
                                "Consider calling option --force.");
            } else {
                System.out.println(
                        "The following repos are affected and have " +
                        "uncommitted changes: " + Strings.listing(uncommittedChangedRepoNames, ", ") + ".\n" +
                        "Force checkout.");
                List<RepoConfig> reposWithModifiedFiles = Repos.selectReposWithModifiedFiles(toBeProcessedRepos);
                if (!reposWithModifiedFiles.isEmpty()) {
                    throw new CommandExecutorException("There are modified files in the following repos: "
                            + Strings.listing(RepoConfigs.getRepoNames(reposWithModifiedFiles), ", ") + "."
                    );
                }
            }
        }

    }

    private String getCliParameterFeature(CliCall cliCall) {
        return cliCall.getCliResult().getParameterParserResult().getParameterList().get(0);
    }

    private FeatureInfo getDestinationFeatureInfo(FeatureInfo sourceFeatureInfo, String destinationFeatureName)
            throws CommandExecutorException {
        FeatureInventory featureInventory = sourceFeatureInfo.getFeatureInventory();

        if (!featureInventory.hasFeatureName(destinationFeatureName))
            throw new CommandExecutorException("No such feature with name [" + destinationFeatureName + "].");

        return FeatureInfo.createFromPreexisting(
                sourceFeatureInfo,
                Feature.createFeatureByName(destinationFeatureName));
    }

    private void checkoutBase(BranchChangeSet branchChangeSet, FeatureInfo sourceFeatureInfo) {
        for (RepoConfig repoConfig : branchChangeSet.getCheckoutToBaseBranches()) {
            DebugOut.println("checkout base branch on repo [" + repoConfig.getRepoName() + "].");
            RepoProperties repoProperties = new RepoProperties(repoConfig, sourceFeatureInfo);
            try {
                Git.checkout(repoProperties.getRepoPath(), repoProperties.getBaseBranchName(), isVerbose());
            } catch (GitException e) {
                throw new MetaRuntimeException(e.getMessage(), e);
            }
        }
    }

    private void checkoutBase(FeatureInfo sourceFeatureInfo) {
        if (!sourceFeatureInfo.hasFeature()) return;

        List<RepoConfig> repoConfigs = sourceFeatureInfo.getRelatedRepoConfigs();
        for (RepoConfig repoConfig : repoConfigs) {
            RepoProperties repoProperties = new RepoProperties(repoConfig, sourceFeatureInfo);
            try {
                Git.checkout(repoProperties.getRepoPath(), repoProperties.getBaseBranchName(), isVerbose());
            } catch (GitException e) {
                throw new MetaRuntimeException(e.getMessage(), e);
            }
        }
    }

    private void checkoutFeature(BranchChangeSet branchChangeSet, FeatureInfo destinationFeatureInfo) {
        for (RepoConfig repoConfig : branchChangeSet.getCheckoutNewFeatureBranches()) {
            DebugOut.println("checkout featureBranch [" + destinationFeatureInfo.getFeature().getFeatureBranchName().getBranchName()
                    + "] on repo [" + repoConfig.getRepoName() + "].");
            RepoProperties repoProperties = new RepoProperties(repoConfig, destinationFeatureInfo);
            try {
                Git.checkout(repoProperties.getRepoPath(), repoProperties.getIntendedBranchName(), isVerbose());
            } catch (GitException e) {
                throw new MetaRuntimeException(e.getMessage(), e);
            }
        }
    }

    private void checkoutFeature(FeatureInfo destFeatureInfo) {
        List<RepoConfig> repoConfigs = destFeatureInfo.getRelatedRepoConfigs();
        for (RepoConfig repoConfig : repoConfigs) {
            RepoProperties repoProperties = new RepoProperties(repoConfig, destFeatureInfo);
            try {
                Git.checkout(repoProperties.getRepoPath(), repoProperties.getIntendedBranchName(), false);
            } catch (GitException e) {
                throw new MetaRuntimeException(e.getMessage(), e);
            }
        }
    }

    private List<RepoConfig> determineAffectedRepos(
            FeatureInfo sourceFeatureInfo, FeatureInfo destinationFeatureInfo) {

        List<RepoConfig> affectedRepos = new ArrayList<>(sourceFeatureInfo.getRelatedRepoConfigs());
        affectedRepos = ListHelper.addIfNotYetContained(affectedRepos, destinationFeatureInfo.getRelatedRepoConfigs());

        return affectedRepos;
    }

    private boolean isVerbose() {
        return ExecutionContext.isVerbose() || ExecutionContext.isDebug();
    }

}
