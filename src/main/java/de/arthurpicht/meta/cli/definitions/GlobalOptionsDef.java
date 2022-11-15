package de.arthurpicht.meta.cli.definitions;

import de.arthurpicht.cli.option.ManOption;
import de.arthurpicht.cli.option.OptionBuilder;
import de.arthurpicht.cli.option.Options;
import de.arthurpicht.cli.option.VersionOption;

public class GlobalOptionsDef {

    public static final String STACKTRACE = "stacktrace";
    public static final String META_DIR = "metaDir";
    public static final String VERBOSE = "verbose";

    public static Options get() {

        return new Options()
                .add(new VersionOption())
                .add(new ManOption())
                .add(new OptionBuilder()
                        .withShortName('s')
                        .withLongName("stacktrace")
                        .withDescription("Show stacktrace when running on error.")
                        .build(STACKTRACE))
                .add(new OptionBuilder()
                        .withShortName('d')
                        .withLongName("metaDir")
                        .withArgumentName("metaDir")
                        .withDescription("meta directory")
                        .build(META_DIR))
                .add(new OptionBuilder()
                        .withLongName("verbose")
                        .withDescription("verbose output")
                        .build(VERBOSE));
    }

}
