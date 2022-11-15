package de.arthurpicht.meta.cli;

import com.diogonunes.jcolor.Ansi;
import de.arthurpicht.cli.*;
import de.arthurpicht.cli.command.Commands;
import de.arthurpicht.cli.command.InfoDefaultCommand;
import de.arthurpicht.cli.common.UnrecognizedArgumentException;
import de.arthurpicht.cli.option.ManOption;
import de.arthurpicht.cli.option.OptionBuilder;
import de.arthurpicht.cli.option.Options;
import de.arthurpicht.cli.option.VersionOption;
import de.arthurpicht.meta.Const;
import de.arthurpicht.meta.cli.definitions.*;
import de.arthurpicht.meta.cli.output.Colors;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.utils.core.strings.Strings;

public class Meta {

    public static final String GLOBAL_OPTION__STACKTRACE = "stacktrace";
    public static final String GLOBAL_OPTION__META_DIR = "metaDir";
    public static final String GLOBAL_OPTION__VERBOSE = "verbose";

    private static Cli createCli() {

        Options globalOptions = new Options()
                .add(new VersionOption())
                .add(new ManOption())
                .add(new OptionBuilder().withShortName('s').withLongName("stacktrace").withDescription("Show stacktrace when running on error.").build(GLOBAL_OPTION__STACKTRACE))
                .add(new OptionBuilder().withShortName('d').withLongName("metaDir").withArgumentName("metaDir").withDescription("meta directory").build(GLOBAL_OPTION__META_DIR))
                .add(new OptionBuilder().withLongName("verbose").withDescription("verbose output").build(GLOBAL_OPTION__VERBOSE));

        Commands commands = new Commands();
        commands.setDefaultCommand(new InfoDefaultCommand());
        commands.add(StatusDef.getCommandSequence());
        commands.add(CloneDef.getCommandSequence());
        commands.add(FetchDef.getCommandSequence());
        commands.add(PullDef.getCommandSequence());
        commands.add(FeatureShowDef.getCommandSequence());
        commands.add(FeatureCheckoutDef.getCommandSequence());
        commands.add(FeatureResetDef.getCommandSequence());

        CliDescription cliDescription = new CliDescriptionBuilder()
                .withDescription("meta\nhttps://github.com/arthurpicht/meta")
                .withVersionByTag(Const.VERSION_TAG, Const.VERSION_DATE)
                .build("meta");

        return new CliBuilder()
                .withGlobalOptions(globalOptions)
                .withCommands(commands)
                .withAutoHelp()
                .build(cliDescription);
    }

    public static void main(String[] args) {

        Cli cli = createCli();
        CliCall cliCall = null;
        try {
            cliCall = cli.parse(args);
        } catch (UnrecognizedArgumentException e) {
            System.out.println(e.getExecutableName() + " call syntax error. " + e.getMessage());
            System.out.println(e.getCallString());
            System.out.println(e.getCallPointerString());
            System.exit(1);
        }

        boolean showStacktrace = cliCall.getOptionParserResultGlobal().hasOption(GLOBAL_OPTION__STACKTRACE);

        try {
            cli.execute(cliCall);
        } catch (CommandExecutorException | MetaRuntimeException e) {
            if (e.getCause() != null) {
                errorOut(e.getCause(), showStacktrace);
                System.exit(10);
            }
            if (Strings.isSpecified(e.getMessage())) {
                errorOut(e, showStacktrace);
                System.exit(1);
            }
            System.exit(1);
        } catch (RuntimeException e) {
            errorOut(e, showStacktrace);
            System.exit(11);
        }
    }

    private static void errorOut(Throwable e, boolean showStacktrace) {
        System.out.println();
        System.out.println(Ansi.colorize("ERROR. OPERATION ABORTED.", Colors.redText));
        System.out.println(e.getMessage());
        if (showStacktrace) e.printStackTrace();
    }

}
