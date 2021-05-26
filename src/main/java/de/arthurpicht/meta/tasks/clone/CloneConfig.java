package de.arthurpicht.meta.tasks.clone;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.config.ProjectConfig;

public class CloneConfig {

    private final ProjectConfig projectConfig;
    private final boolean verbose;
    private final boolean stacktrace;
    private final boolean cicd;

    public CloneConfig(ProjectConfig projectConfig, boolean verbose, boolean stacktrace, boolean cicd) {
        this.projectConfig = projectConfig;
        this.verbose = verbose;
        this.stacktrace = stacktrace;
        this.cicd = cicd;
    }

    public static CloneConfig getInstance(ProjectConfig projectConfig, boolean cicd) {
        return new CloneConfig(
                projectConfig,
                ExecutionContext.isVerbose(),
                ExecutionContext.isStacktrace(),
                cicd);
    }

    public ProjectConfig getProjectConfig() {
        return projectConfig;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public boolean isStacktrace() {
        return stacktrace;
    }

    public boolean isCicd() {
        return cicd;
    }
}
