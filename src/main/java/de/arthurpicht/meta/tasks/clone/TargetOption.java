package de.arthurpicht.meta.tasks.clone;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.Meta;
import de.arthurpicht.meta.cli.executor.CloneExecutor;

public class TargetOption {

    private final CliCall cliCall;

    public TargetOption(CliCall cliCall) {
        this.cliCall = cliCall;
    }

    public boolean isSpecified() {
        return cliCall.getOptionParserResultSpecific().hasOption(Meta.OPTION_CLONE_TARGET);
    }

    public CloneExecutor.Target getArgument() throws CommandExecutorException {
        if (!isSpecified()) throw new IllegalStateException("CLI call has no target option.");

        String targetSpec = cliCall.getOptionParserResultSpecific().getValue(Meta.OPTION_CLONE_TARGET);
        if (targetSpec.equalsIgnoreCase(CloneExecutor.Target.DEV.name())) {
            return CloneExecutor.Target.DEV;
        } else if (targetSpec.equalsIgnoreCase(CloneExecutor.Target.PROD.name())) {
            return CloneExecutor.Target.PROD;
        } else {
            throw new CommandExecutorException("Illegal value for option [" + Meta.OPTION_CLONE_TARGET + "]: '" + targetSpec + "'." +
                    " Must be either <dev> (default) or <prod>.");
        }
    }

}
