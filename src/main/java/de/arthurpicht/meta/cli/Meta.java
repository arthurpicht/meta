package de.arthurpicht.meta.cli;

import de.arthurpicht.cli.*;
import de.arthurpicht.cli.command.CommandSequenceBuilder;
import de.arthurpicht.cli.command.Commands;
import de.arthurpicht.cli.command.InfoDefaultCommand;
import de.arthurpicht.cli.common.UnrecognizedArgumentException;
import de.arthurpicht.cli.option.ManOption;
import de.arthurpicht.cli.option.OptionBuilder;
import de.arthurpicht.cli.option.Options;
import de.arthurpicht.cli.option.VersionOption;
import de.arthurpicht.meta.cli.executor.CloneExecutor;

public class Meta {

    public static final String OPTION_STACKTRACE = "stacktrace";
    public static final String OPTION_META_DIR = "metaDir";

    public static final String OPTION_CICD = "cicd";

    private static Cli createCli() {

        Options globalOptions = new Options()
                .add(new VersionOption())
                .add(new ManOption())
                .add(new OptionBuilder().withShortName('s').withLongName("stacktrace").withDescription("Show stacktrace when running on error.").build(OPTION_STACKTRACE))
                .add(new OptionBuilder().withShortName('d').withLongName("metaDir").withArgumentName("metaDir").withDescription("meta directory").build(OPTION_META_DIR));

        Commands commands = new Commands();

        commands.setDefaultCommand(new InfoDefaultCommand());

        Options cloneOptions = new Options()
                .add(new OptionBuilder().withShortName('c').withLongName("cicd").withDescription("Use read only repos for CICD.").build(OPTION_CICD));

        commands.add(new CommandSequenceBuilder()
                .addCommands("clone")
                .withSpecificOptions(cloneOptions)
                .withCommandExecutor(new CloneExecutor())
                .withDescription("Clones repos.")
                .build()
        );

        CliDescription cliDescription = new CliDescriptionBuilder()
                .withDescription("meta\nhttps://github.com/arthurpicht/meta")
                .withVersionByTag("0.1-SNAPSHOT", "2021-05-18")
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
        } catch (CommandExecutorException | RuntimeException e) {
            System.out.println(e.getMessage());
            if (showStacktrace) e.printStackTrace();
            System.exit(1);
        }
    }

}