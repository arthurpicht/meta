package de.arthurpicht.meta.tasks.status;

import de.arthurpicht.meta.cli.output.Output;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.RepoConfig;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.git.GitException;
import de.arthurpicht.meta.tasks.RepoExecutor;
import de.arthurpicht.meta.tasks.TaskSummary;
import de.arthurpicht.meta.tasks.feature.FeatureInfo;

public class StatusRepoExecutor extends RepoExecutor {

    private final FeatureInfo featureInfo;

    public StatusRepoExecutor(FeatureInfo featureInfo) {
        this.featureInfo = featureInfo;
    }

    @Override
    public void execute(RepoConfig repoConfig, Target target, TaskSummary taskSummary) {
        try {
            RepoProperties repoProperties = new RepoProperties(repoConfig, this.featureInfo);
            RepoStatusOutput.output(repoProperties);
        } catch (MetaRuntimeException e) {
            Output.error(repoConfig.getRepoName(), e.getMessage());
        } catch (GitException | RuntimeException e) {
            Output.error(repoConfig.getRepoName(), "Error: " + e.getMessage());
        }
    }

    @Override
    public boolean showSummary() {
        return false;
    }

}
