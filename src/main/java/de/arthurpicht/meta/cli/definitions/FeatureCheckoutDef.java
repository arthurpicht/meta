package de.arthurpicht.meta.cli.definitions;

import de.arthurpicht.cli.command.CommandSequence;
import de.arthurpicht.cli.command.CommandSequenceBuilder;
import de.arthurpicht.cli.option.OptionBuilder;
import de.arthurpicht.cli.option.Options;
import de.arthurpicht.cli.parameter.ParametersOne;
import de.arthurpicht.meta.cli.executor.FeatureCheckoutExecutor;

public class FeatureCheckoutDef {

    public static final String OPTION_FORCE = "FEATURE_CHECKOUT__OPTION__FORCE";

    public static CommandSequence get() {

        Options options = new Options()
                .add(new OptionBuilder()
                        .withShortName('f')
                        .withLongName("force")
                        .withDescription("force checkout even on uncommitted changes.")
                        .build(OPTION_FORCE));

        return new CommandSequenceBuilder()
                .addCommands("feature", "checkout")
                .withCommandExecutor(new FeatureCheckoutExecutor())
                .withSpecificOptions(options)
                .withParameters(new ParametersOne("feature", "name of feature"))
                .withDescription("Checkout feature.")
                .build();
    }

}
