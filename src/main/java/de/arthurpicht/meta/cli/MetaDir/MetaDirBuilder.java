package de.arthurpicht.meta.cli.MetaDir;

import de.arthurpicht.cli.option.OptionParserResult;
import de.arthurpicht.meta.Const;
import de.arthurpicht.meta.cli.Meta;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.meta.helper.FilesHelper;
import de.arthurpicht.utils.core.strings.Strings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MetaDirBuilder {

    public static MetaDir obtain(OptionParserResult optionParserResultGlobal) {

        MetaDir metaDir = obtainMetaDir(optionParserResultGlobal);
        assertExistenceOfMetaDir(metaDir);
        assertContainsMetaConf(metaDir);
        return metaDir;
    }

    private static MetaDir obtainMetaDir(OptionParserResult optionParserResultGlobal) {
        String metaDirString;
        MetaDirSpecifier metaDirSpecifier;
        if (optionParserResultGlobal.hasOption(Meta.OPTION_META_DIR)) {
            metaDirString = optionParserResultGlobal.getValue(Meta.OPTION_META_DIR);
            metaDirSpecifier = MetaDirSpecifier.CLI;
        } else if (Strings.isSpecified(System.getenv(Const.META_DIR__ENV_VAR))) {
            metaDirString = System.getenv(Const.META_DIR__ENV_VAR);
            metaDirSpecifier = MetaDirSpecifier.ENV_VAR;
        } else {
            metaDirString = FilesHelper.getWorkingDir().toString();
            metaDirSpecifier = MetaDirSpecifier.WORKING_DIR;
        }

        Path metaDir = Paths.get(metaDirString).normalize();
        return new MetaDir(metaDir, metaDirSpecifier);
    }

    private static void assertExistenceOfMetaDir(MetaDir metaDir) {
        if (!Files.exists(metaDir.asPath()) || !Files.isDirectory(metaDir.asPath()))
            throw new MetaRuntimeException("Meta directory as specified by " + metaDir.getSpecifier().getText() + " not found: [" + metaDir.asPath() + "].");
    }

    private static void assertContainsMetaConf(MetaDir metaDir) {
        Path metaConf = metaDir.asPath().resolve(Const.META_CONF__FILE_NAME);
        if (!Files.exists(metaConf) || !Files.isRegularFile(metaConf))
            throw new MetaRuntimeException("No " + Const.META_CONF__FILE_NAME + " [" + metaConf.toAbsolutePath() + "] found.");
    }

}
