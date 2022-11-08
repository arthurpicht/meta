package de.arthurpicht.meta.cli;

import com.diogonunes.jcolor.Ansi;
import de.arthurpicht.cli.*;
import de.arthurpicht.cli.command.CommandSequenceBuilder;
import de.arthurpicht.cli.command.Commands;
import de.arthurpicht.cli.command.InfoDefaultCommand;
import de.arthurpicht.cli.common.UnrecognizedArgumentException;
import de.arthurpicht.cli.option.*;
import de.arthurpicht.cli.parameter.ParametersN;
import de.arthurpicht.cli.parameter.ParametersOne;
import de.arthurpicht.meta.Const;
import de.arthurpicht.meta.cli.executor.*;
import de.arthurpicht.meta.cli.output.Colors;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.utils.core.strings.Strings;

public class Meta {

    public static final String OPTION_STACKTRACE = "stacktrace";
    public static final String OPTION_META_DIR = "metaDir";
    public static final String OPTION_VERBOSE = "verbose";

    public static final String OPTION_CLONE_TARGET = "target";

    private static Cli createCli() {

        Options globalOptions = new Options()
                .add(new VersionOption())
                .add(new ManOption())
                .add(new OptionBuilder().withShortName('s').withLongName("stacktrace").withDescription("Show stacktrace when running on error.").build(OPTION_STACKTRACE))
                .add(new OptionBuilder().withShortName('d').withLongName("metaDir").withArgumentName("metaDir").withDescription("meta directory").build(OPTION_META_DIR))
                .add(new OptionBuilder().withLongName("verbose").withDescription("verbose output").build(OPTION_VERBOSE));

        Commands commands = new Commands();

        commands.setDefaultCommand(new InfoDefaultCommand());

        Options cloneOptions = new Options()
                .add(new OptionBuilder().withShortName('t').withLongName("target").withArgumentName("target").withDescription("Target environment. Either <dev> (default) or <prod>.").build(OPTION_CLONE_TARGET));

        commands.add(new CommandSequenceBuilder()
                .addCommands("clone")
                .withSpecificOptions(cloneOptions)
                .withCommandExecutor(new CloneExecutor())
                .withDescription("Clones all repos for respective target.")
                .build()
        );

        commands.add(new CommandSequenceBuilder()
                .addCommand("status")
                .withCommandExecutor(new StatusExecutor())
                .withDescription("Shows status of all repos.")
                .build()
        );

        commands.add(new CommandSequenceBuilder()
                .addCommand("fetch")
                .withCommandExecutor(new FetchExecutor())
                .withDescription("Execute fetch on all repos.")
                .build()
        );

        commands.add(new CommandSequenceBuilder()
                .addCommand("pull")
                .withCommandExecutor(new PullExecutor())
                .withDescription("Execute pull on all repos without local changes. Else execute fetch.")
                .build()
        );

        commands.add(new CommandSequenceBuilder()
                .addCommands("feature", "show")
                .withCommandExecutor(new FeatureShowExecutor())
                .withDescription("Show all features.")
                .build()
        );

        commands.add(new CommandSequenceBuilder()
                .addCommands("feature", "checkout")
                .withCommandExecutor(new FeatureCheckoutExecutor())
                .withParameters(new ParametersOne())
                .withDescription("Checkout feature.")
                .build()
        );

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

        boolean showStacktrace = cliCall.getOptionParserResultGlobal().hasOption(OPTION_STACKTRACE);

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
