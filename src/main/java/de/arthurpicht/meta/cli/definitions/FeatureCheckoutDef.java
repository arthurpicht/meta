package de.arthurpicht.meta.cli.definitions;

import de.arthurpicht.cli.command.CommandSequence;
import de.arthurpicht.cli.command.CommandSequenceBuilder;
import de.arthurpicht.cli.parameter.ParametersOne;
import de.arthurpicht.meta.cli.executor.FeatureCheckoutExecutor;

public class FeatureCheckoutDef {

    public static CommandSequence get() {

        return new CommandSequenceBuilder()
                .addCommands("feature", "checkout")
                .withCommandExecutor(new FeatureCheckoutExecutor())
                .withParameters(new ParametersOne())
                .withDescription("Checkout feature.")
                .build();
    }

}
