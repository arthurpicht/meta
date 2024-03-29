package de.arthurpicht.meta.cli;

import de.arthurpicht.cli.*;
import de.arthurpicht.cli.command.Commands;
import de.arthurpicht.cli.command.InfoDefaultCommand;
import de.arthurpicht.cli.common.UnrecognizedArgumentException;
import de.arthurpicht.console.Console;
import de.arthurpicht.console.config.ConsoleConfigurationBuilder;
import de.arthurpicht.console.message.format.Format;
import de.arthurpicht.meta.Const;
import de.arthurpicht.meta.cli.definitions.*;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.utils.core.strings.Strings;

public class Meta {

    private static Cli createCli() {

        Commands commands = new Commands();
        commands.setDefaultCommand(new InfoDefaultCommand());
        commands.add(StatusDef.get());
        commands.add(CloneDef.get());
        commands.add(FetchDef.get());
        commands.add(PullDef.get());
        commands.add(FeatureShowDef.get());
        commands.add(FeatureCheckoutDef.get());
        commands.add(FeatureResetDef.get());

        CliDescription cliDescription = new CliDescriptionBuilder()
                .withDescription("meta\nhttps://github.com/arthurpicht/meta")
                .withVersionByTag(Const.VERSION_TAG, Const.VERSION_DATE)
                .build("meta");

        return new CliBuilder()
                .withGlobalOptions(GlobalOptionsDef.get())
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

        boolean showStacktrace = cliCall.getOptionParserResultGlobal().hasOption(GlobalOptionsDef.STACKTRACE);

        ConsoleConfigurationBuilder consoleConfigurationBuilder = new ConsoleConfigurationBuilder();
        if (isNoColor(cliCall)) consoleConfigurationBuilder.withSuppressedColors();
        Console.configure(consoleConfigurationBuilder.build());

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
        Console.error("ERROR. OPERATION ABORTED.", Format.RED_TEXT());
        Console.error(e.getMessage());
        if (showStacktrace) Console.printStackTrace(e);
    }

    private static boolean isNoColor(CliCall cliCall) {
        if (cliCall.getOptionParserResultGlobal().hasOption(GlobalOptionsDef.NO_COLOR)) return true;
        try {
            if (System.getenv("NO_COLOR") != null) return true;
        } catch (SecurityException e) {
            // ignored
        }
        return false;
    }

}
