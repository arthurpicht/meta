package de.arthurpicht.meta.cli.target;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.meta.cli.Meta;
import de.arthurpicht.meta.exception.MetaRuntimeException;

public class TargetOption {

    private final CliCall cliCall;

    public TargetOption(CliCall cliCall) {
        this.cliCall = cliCall;
    }

    public boolean isSpecified() {
        return cliCall.getOptionParserResultSpecific().hasOption(Meta.OPTION_CLONE_TARGET);
    }

    public Target getArgument() {
        if (!isSpecified()) throw new IllegalStateException("CLI call has no target option.");

        String targetSpec = cliCall.getOptionParserResultSpecific().getValue(Meta.OPTION_CLONE_TARGET);
        if (targetSpec.equalsIgnoreCase(Target.DEV.name())) {
            return Target.DEV;
        } else if (targetSpec.equalsIgnoreCase(Target.PROD.name())) {
            return Target.PROD;
        } else {
            throw new MetaRuntimeException("Illegal value for option [" + Meta.OPTION_CLONE_TARGET + "]: '" + targetSpec + "'." +
                    " Must be either <dev> (default) or <prod>.");
        }
    }

}
