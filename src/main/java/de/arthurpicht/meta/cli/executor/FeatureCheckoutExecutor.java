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
import de.arthurpicht.meta.tasks.feature.FeatureInfo;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureInventory;
import de.arthurpicht.meta.tasks.feature.scanner.FeatureScanner;
import de.arthurpicht.meta.tasks.status.RepoProperties;
import de.arthurpicht.utils.core.strings.Strings;

import java.nio.file.Path;
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

        FeatureInfo sourceFeatureInfo = FeatureInfo.createFromPersistence(metaConfig, target);
        FeatureInventory featureInventory
                = sourceFeatureInfo.hasFeature() ?
                sourceFeatureInfo.getFeatureInventory() : FeatureScanner.scan(metaConfig, target);

        String destinationFeatureName = cliCall.getCliResult().getParameterParserResult().getParameterList().get(0);
        if (!featureInventory.hasFeatureName(destinationFeatureName))
            throw new CommandExecutorException("No such feature with name [" + destinationFeatureName + "].");
        FeatureInfo destinationFeatureInfo = createFeatureInfoFromPreexisting(featureInventory, destinationFeatureName);

        List<String> affectedRepos = determineAffectedRepos(sourceFeatureInfo, destinationFeatureInfo);
        List<String> uncommittedChangedRepos = extractReposWithUncommittedChanges(metaConfig, affectedRepos);
        if (!uncommittedChangedRepos.isEmpty())
            throw new CommandExecutorException(
                    "Cannot checkout feature. The following repos are affected and have " +
                    "uncommitted changes: " + Strings.listing(uncommittedChangedRepos, ", ") + ".\n" +
                    "Use option --force to ignore that.");

        checkoutBase(metaConfig, sourceFeatureInfo);
        checkoutFeature(metaConfig, sourceFeatureInfo);

        System.out.println("Successfully checked out feature [" + destinationFeatureName + "].");
    }

    private void checkoutBase(MetaConfig metaConfig, FeatureInfo sourceFeatureInfo) {
        if (!sourceFeatureInfo.hasFeature()) return;

        List<String> repos = sourceFeatureInfo.getRelatedRepos();
        for (String repo : repos) {
            RepoConfig repoConfig = metaConfig.getRepoConfig(repo);
            RepoProperties repoProperties = new RepoProperties(repoConfig, sourceFeatureInfo);
            try {
                Git.checkout(repoProperties.getRepoPath(), repoProperties.getBaseBranchName(), false);
            } catch (GitException e) {
                throw new MetaRuntimeException(e.getMessage(), e);
            }
        }
    }

    private void checkoutFeature(MetaConfig metaConfig, FeatureInfo destFeatureInfo) {
        List<String> repos = destFeatureInfo.getRelatedRepos();
        for (String repo : repos) {
            RepoConfig repoConfig = metaConfig.getRepoConfig(repo);
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

    private List<String> determineAffectedRepos(
            FeatureInfo sourceFeatureInfo, FeatureInfo destinationFeatureInfo) {

        List<String> affectedRepos = sourceFeatureInfo.hasFeature() ?
                sourceFeatureInfo.getRelatedRepos() :
                new ArrayList<>();
        affectedRepos.addAll(destinationFeatureInfo.getRelatedRepos());

        return affectedRepos;
    }

    private List<String> extractReposWithUncommittedChanges(MetaConfig metaConfig, List<String> affectedRepos) {
        List<String> reposWithUncommittedChanges = new ArrayList<>();
        for (String repo : affectedRepos) {
            RepoConfig repoConfig = metaConfig.getRepoConfig(repo);
            if (hasUncommittedChanges(repoConfig.getRepoPath()))
                reposWithUncommittedChanges.add(repo);
        }
        return reposWithUncommittedChanges;
    }

    private boolean hasUncommittedChanges(Path repoPath) {
        try {
            return Git.hasUncommittedChanges(repoPath);
        } catch (GitException e) {
            throw new MetaRuntimeException(e.getMessage(), e);
        }
    }

}
