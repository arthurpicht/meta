package de.arthurpicht.meta.cli.definitions;

import de.arthurpicht.cli.command.CommandSequence;
import de.arthurpicht.cli.command.CommandSequenceBuilder;
import de.arthurpicht.meta.cli.executor.StatusExecutor;

public class StatusDef {

    public static CommandSequence getCommandSequence() {
        return new CommandSequenceBuilder()
                .addCommand("status")
                .withCommandExecutor(new StatusExecutor())
                .withDescription("Shows status of all repos.")
                .build();
    }

}
