package de.arthurpicht.meta.tasks.clone;

import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.config.ProjectConfig;
import de.arthurpicht.meta.cli.target.Target;

public class CloneConfig {

    private final ProjectConfig projectConfig;
    private final boolean verbose;
    private final boolean stacktrace;
    private final Target target;

    public CloneConfig(ProjectConfig projectConfig, boolean verbose, boolean stacktrace, Target target) {
        this.projectConfig = projectConfig;
        this.verbose = verbose;
        this.stacktrace = stacktrace;
        this.target = target;
    }

    public static CloneConfig getInstance(ProjectConfig projectConfig, Target target) {
        return new CloneConfig(
                projectConfig,
                ExecutionContext.isVerbose(),
                ExecutionContext.isStacktrace(),
                target);
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

    public Target getTarget() {
        return this.target;
    }

    public boolean isTargetProd() {
        return this.target.equals(Target.PROD);
    }

    public boolean isTargetDev() {
        return this.target.equals(Target.DEV);
    }

}
