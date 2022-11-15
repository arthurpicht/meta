package de.arthurpicht.meta.cli.definitions;

import de.arthurpicht.cli.command.CommandSequence;
import de.arthurpicht.cli.command.CommandSequenceBuilder;
import de.arthurpicht.cli.option.OptionBuilder;
import de.arthurpicht.cli.option.Options;
import de.arthurpicht.meta.cli.executor.FeatureResetExecutor;

public class FeatureResetDef {

    public static final String OPTION_ALL = "FEATURE_RESET__OPTION__ALL";
    public static final String OPTION_FORCE = "FEATURE_RESET__OPTION__FORCE";

    public static CommandSequence get() {

        Options options = new Options()
                .add(new OptionBuilder()
                        .withShortName('a')
                        .withLongName("all")
                        .withDescription("Reset all repos.")
                        .build(OPTION_ALL))
                .add(new OptionBuilder()
                        .withShortName('f')
                        .withLongName("force")
                        .withDescription("Force reset, ignore warnings.")
                        .build(OPTION_FORCE));

        return new CommandSequenceBuilder()
                .addCommands("feature", "reset")
                .withCommandExecutor(new FeatureResetExecutor())
                .withSpecificOptions(options)
                .withDescription("Reset branches to base.")
                .build();
    }

}
