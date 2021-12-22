package de.arthurpicht.meta.cli.target;

import de.arthurpicht.cli.CliCall;
import de.arthurpicht.meta.cli.Meta;

public class TargetOption {

    private final CliCall cliCall;

    public TargetOption(CliCall cliCall) {
        this.cliCall = cliCall;
    }

    public boolean isSpecified() {
        return cliCall.getOptionParserResultSpecific().hasOption(Meta.OPTION_CLONE_TARGET);
    }

    public String getTargetArgument() {
        if (!isSpecified()) throw new IllegalStateException("CLI call has no target option.");
        return cliCall.getOptionParserResultSpecific().getValue(Meta.OPTION_CLONE_TARGET);
    }

}
