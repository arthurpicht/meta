package de.arthurpicht.meta;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.ExecutionContext;
import de.arthurpicht.meta.cli.feature.Feature;
import de.arthurpicht.meta.cli.target.ProjectTarget;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.config.MetaConfig;
import de.arthurpicht.meta.config.MetaConfigFactory;
import de.arthurpicht.meta.config.exceptions.ConfigurationException;

public class Context {

    private final MetaConfig metaConfig;
    private final Target target;
//    private final Feature feature;

    public Context(CliCall cliCall) throws CommandExecutorException {
        ExecutionContext.init(cliCall);
        this.metaConfig = initMetaConfig();
        this.target = ProjectTarget.obtainInitializedTarget(metaConfig.getGeneralConfig().getTargets());
//        this.feature = Feature.
    }

    public MetaConfig getMetaConfig() {
        return metaConfig;
    }

    public Target getTarget() {
        return target;
    }

    private MetaConfig initMetaConfig() throws CommandExecutorException {
        try {
            return MetaConfigFactory.create(ExecutionContext.getMetaDirAsPath());
        } catch (ConfigurationException e) {
            throw new CommandExecutorException(e.getMessage(), e);
        }
    }

}
