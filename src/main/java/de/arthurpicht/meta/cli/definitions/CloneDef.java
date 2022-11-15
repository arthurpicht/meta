package de.arthurpicht.meta.cli.definitions;

import de.arthurpicht.cli.command.CommandSequence;
import de.arthurpicht.cli.command.CommandSequenceBuilder;
import de.arthurpicht.cli.option.OptionBuilder;
import de.arthurpicht.cli.option.Options;
import de.arthurpicht.meta.cli.executor.CloneExecutor;

public class CloneDef {

    public static final String CLONE__OPTION__TARGET = "target";

    public static CommandSequence getCommandSequence() {
        Options cloneOptions = new Options()
                .add(new OptionBuilder()
                        .withShortName('t')
                        .withLongName("target")
                        .withArgumentName("target")
                        .withDescription("Target environment. Either <dev> (default) or <prod>.")
                        .build(CLONE__OPTION__TARGET));

        return new CommandSequenceBuilder()
                .addCommands("clone")
                .withSpecificOptions(cloneOptions)
                .withCommandExecutor(new CloneExecutor())
                .withDescription("Clones all repos for respective target.")
                .build();

    }

}
