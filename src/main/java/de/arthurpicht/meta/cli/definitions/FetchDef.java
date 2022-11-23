package de.arthurpicht.meta.cli.definitions;

import de.arthurpicht.cli.command.CommandSequence;
import de.arthurpicht.cli.command.CommandSequenceBuilder;
import de.arthurpicht.meta.cli.executor.FetchExecutor;

public class FetchDef {

    public static CommandSequence get() {

        return new CommandSequenceBuilder()
                .addCommand("fetch")
                .withCommandExecutor(new FetchExecutor())
                .withDescription("Execute fetch on all repos.")
                .build();
    }

}
