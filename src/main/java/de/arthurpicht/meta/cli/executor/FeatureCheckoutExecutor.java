package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.cli.target.ProjectTarget;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.git.Git;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.git.Repos;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureInventory;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureScanner;
import de.arthurpicht.meta.tasks.status.RepoProperties;
import de.arthurpicht.utils.core.strings.Strings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.assertGitInstalled;
import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.initMetaConfig;

public class FeatureCheckoutExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {

        assertGitInstalled();

        ExecutionContext.init(cliCall);
        MetaConfig metaConfig = initMetaConfig();
        Target target = ProjectTarget.obtainInitializedTarget(metaConfig.getGeneralConfig().getTargets());

        FeatureInfo sourceFeatureInfo = FeatureInfo.createFromPersistence(metaConfig, target);
        FeatureInventory featureInventory
                = sourceFeatureInfo.hasFeature() ?
                sourceFeatureInfo.getFeatureInventory() : FeatureScanner.scan(metaConfig, target);

        String destinationFeatureName = cliCall.getCliResult().getParameterParserResult().getParameterList().get(0);
        if (!featureInventory.hasFeatureName(destinationFeatureName))
            throw new CommandExecutorException("No such feature with name [" + destinationFeatureName + "].");
        FeatureInfo destinationFeatureInfo = createFeatureInfoFromPreexisting(featureInventory, destinationFeatureName);

        List<RepoConfig> affectedRepos = determineAffectedRepos(sourceFeatureInfo, destinationFeatureInfo);
        List<RepoConfig> uncommittedChangedRepos = Repos.selectReposWithUncommittedChanges(affectedRepos);

        if (!uncommittedChangedRepos.isEmpty()) {
            List<String> uncommittedChangedRepoNames = uncommittedChangedRepos.stream()
                    .map(RepoConfig::getRepoName)
                    .collect(Collectors.toList());
            throw new CommandExecutorException(
                    "Cannot checkout feature. The following repos are affected and have " +
                    "uncommitted changes: " + Strings.listing(uncommittedChangedRepoNames, ", ") + ".\n" +
                    "Consider calling option --force.");
        }

        checkoutBase(sourceFeatureInfo);
        checkoutFeature(sourceFeatureInfo);

        System.out.println("Successfully checked out feature [" + destinationFeatureName + "].");
    }

    private void checkoutBase(FeatureInfo sourceFeatureInfo) {
        if (!sourceFeatureInfo.hasFeature()) return;

        List<RepoConfig> repoConfigs = sourceFeatureInfo.getRelatedRepoConfigs();
        for (RepoConfig repoConfig : repoConfigs) {
            RepoProperties repoProperties = new RepoProperties(repoConfig, sourceFeatureInfo);
            try {
                Git.checkout(repoProperties.getRepoPath(), repoProperties.getBaseBranchName(), false);
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

    private FeatureInfo createFeatureInfoFromPreexisting(FeatureInventory featureInventory, String destinationFeatureName) {
        Feature feature = Feature.createFeatureByName(destinationFeatureName);
        return new FeatureInfo(feature, featureInventory);
    }

    private List<RepoConfig> determineAffectedRepos(
            FeatureInfo sourceFeatureInfo, FeatureInfo destinationFeatureInfo) {

        List<RepoConfig> affectedRepos = sourceFeatureInfo.hasFeature() ?
                sourceFeatureInfo.getRelatedRepoConfigs() :
                new ArrayList<>();
        affectedRepos.addAll(destinationFeatureInfo.getRelatedRepoConfigs());

        return affectedRepos;
    }

}
