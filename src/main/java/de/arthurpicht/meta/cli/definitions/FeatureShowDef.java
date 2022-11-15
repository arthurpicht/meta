package de.arthurpicht.meta.cli.definitions;

import de.arthurpicht.cli.command.CommandSequence;
import de.arthurpicht.cli.command.CommandSequenceBuilder;
import de.arthurpicht.meta.cli.executor.FeatureShowExecutor;

public class FeatureShowDef {

    public static CommandSequence getCommandSequence() {

        return new CommandSequenceBuilder()
                .addCommands("feature", "show")
                .withCommandExecutor(new FeatureShowExecutor())
                .withDescription("Show all features.")
                .build();

    }

}
