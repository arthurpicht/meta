package de.arthurpicht.meta.tasks.clone;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.cli.CommandExecutorException;
import de.arthurpicht.meta.cli.Meta;
import de.arthurpicht.meta.tasks.Target;

public class TargetOption {

    private final CliCall cliCall;

    public TargetOption(CliCall cliCall) {
        this.cliCall = cliCall;
    }

    public boolean isSpecified() {
        return cliCall.getOptionParserResultSpecific().hasOption(Meta.OPTION_CLONE_TARGET);
    }

    public Target getArgument() throws CommandExecutorException {
        if (!isSpecified()) throw new IllegalStateException("CLI call has no target option.");

        String targetSpec = cliCall.getOptionParserResultSpecific().getValue(Meta.OPTION_CLONE_TARGET);
        if (targetSpec.equalsIgnoreCase(Target.DEV.name())) {
            return Target.DEV;
        } else if (targetSpec.equalsIgnoreCase(Target.PROD.name())) {
            return Target.PROD;
        } else {
            throw new CommandExecutorException("Illegal value for option [" + Meta.OPTION_CLONE_TARGET + "]: '" + targetSpec + "'." +
                    " Must be either <dev> (default) or <prod>.");
        }
    }

}
