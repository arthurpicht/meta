package de.arthurpicht.meta.tasks.clone;

import de.arthurpicht.meta.config.ProjectConfig;

public class CloneConfigBuilder {

    private final ProjectConfig projectConfig;
    private boolean verbose;
    private boolean stacktrace;
    private boolean cicd;

    public CloneConfigBuilder(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
        this.verbose = false;
        this.stacktrace = false;
        this.cicd = false;
    }

    public CloneConfigBuilder withVerbose() {
        this.verbose = true;
        return this;
    }

    public CloneConfigBuilder withStacktrace() {
        this.stacktrace = true;
        return this;
    }

    public CloneConfigBuilder withCicd() {
        this.cicd = true;
        return this;
    }

    public CloneConfig build() {
        return new CloneConfig(
                this.projectConfig,
                this.verbose,
                this.stacktrace,
                this.cicd
        );
    }

}
