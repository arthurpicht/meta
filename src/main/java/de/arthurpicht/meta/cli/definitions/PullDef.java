package de.arthurpicht.meta.cli.definitions;

import de.arthurpicht.cli.command.CommandSequence;
import de.arthurpicht.cli.command.CommandSequenceBuilder;
import de.arthurpicht.meta.cli.executor.PullExecutor;

public class PullDef {

    public static CommandSequence get() {
        return new CommandSequenceBuilder()
                .addCommand("pull")
                .withCommandExecutor(new PullExecutor())
                .withDescription("Execute pull on all repos without local changes. Else execute fetch.")
                .build();
    }

}
