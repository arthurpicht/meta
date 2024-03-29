package de.arthurpicht.meta.cli.executor;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutor;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.definitions.FeatureResetDef;
import de.arthurpicht.meta.cli.definitions.GlobalOptionsDef;
import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.cli.persistence.project.FeatureFile;
import de.arthurpicht.meta.cli.target.ProjectTarget;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.config.RepoConfigs;
import de.arthurpicht.meta.git.Repos;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;
import de.arthurpicht.utils.core.strings.Strings;

import java.util.List;

import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.assertGitInstalled;
import static de.arthurpicht.meta.cli.executor.CommandExecutorCommons.initMetaConfig;

public class FeatureResetExecutor implements CommandExecutor {

    @Override
    public void execute(CliCall cliCall) throws CommandExecutorException {
        assertGitInstalled();

        ExecutionContext.init(cliCall);
        MetaConfig metaConfig = initMetaConfig();
        Target target = ProjectTarget.obtainInitializedTarget(metaConfig.getGeneralConfig().getTargets());

        boolean verbose = cliCall.getOptionParserResultGlobal().hasOption(GlobalOptionsDef.VERBOSE);
        boolean all = cliCall.getOptionParserResultSpecific().hasOption(FeatureResetDef.OPTION_ALL);
        boolean force = cliCall.getOptionParserResultSpecific().hasOption(FeatureResetDef.OPTION_FORCE);

        FeatureInfo featureInfo;
        try {
            featureInfo = FeatureInfo.createFromPersistence(metaConfig, target);
        } catch (FeatureInfo.FeatureGoneException e) {
            if (force && all) {
                System.out.println(Output.yellowTag("WARN") + "Configured feature [" + e.getFeature().getName() + "] is gone.");
                Feature feature = Feature.createWithNoFeature();
                featureInfo = new FeatureInfo(feature, e.getFeatureInventory());
                FeatureFile featureFile = new FeatureFile(ExecutionContext.getMetaDirAsPath());
                featureFile.delete();
            } else {
                throw e;
            }
        }


        if (all) {
            resetAll(metaConfig, target, featureInfo, force, verbose);
        } else {
            resetFeature(featureInfo, force, verbose, metaConfig, target);
        }
    }

    private void resetFeature(FeatureInfo featureInfo, boolean force, boolean verbose, MetaConfig metaConfig, Target target)
            throws CommandExecutorException {

        if (!featureInfo.hasFeature())
            throw new CommandExecutorException("There is no feature checked out." +
                    " Consider calling --all to reset all repos.");

        List<RepoConfig> relatedRepoConfigs = featureInfo.getRelatedRepoConfigs();

        reset(relatedRepoConfigs, force, verbose, metaConfig, target);

        System.out.println("All repos related to feature [" + featureInfo.getFeature().getName() + "] are reset to base.");
    }

    private void resetAll(MetaConfig metaConfig, Target target, FeatureInfo featureInfo, boolean force, boolean verbose)
            throws CommandExecutorException {

        List<RepoConfig> repos = metaConfig.getRepoConfigsForTarget(target);
        List<RepoConfig> reposNotOnBaseBranch = Repos.selectReposNotOnBaseBranch(repos, featureInfo);
        reset(reposNotOnBaseBranch, force, verbose, metaConfig, target);

        System.out.println("All repos reset.");
    }

    private void reset(List<RepoConfig> repoConfigs, boolean force, boolean verbose, MetaConfig metaConfig, Target target)
            throws CommandExecutorException {

        List<RepoConfig> changedRepos = Repos.selectReposWithUncommittedChanges(repoConfigs);

        if (!changedRepos.isEmpty()) {

            if (!force) {
                throw new CommandExecutorException("Cannot reset feature. There are changes in the following repos: "
                        + Strings.listing(RepoConfigs.getRepoNames(changedRepos), ", ") + ".");
            } else {
                System.out.println("There are changes in the following repos: "
                        + Strings.listing(RepoConfigs.getRepoNames(changedRepos), ", ") + ". Force reset.");

                List<RepoConfig> reposWithModifiedFiles = Repos.selectReposWithModifiedFiles(changedRepos);
                if (!reposWithModifiedFiles.isEmpty()) {
                    throw new CommandExecutorException("There are modified files in the following repos: "
                            + Strings.listing(RepoConfigs.getRepoNames(reposWithModifiedFiles), ", ") + "."
                    );
                }
            }
        }

        Repos.reset(repoConfigs, verbose, metaConfig, target);

        FeatureFile featureFile = new FeatureFile(ExecutionContext.getMetaDirAsPath());
        featureFile.delete();
    }

}
