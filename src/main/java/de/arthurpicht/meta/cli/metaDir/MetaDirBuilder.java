package de.arthurpicht.meta.cli.metaDir;

import de.arthurpicht.cli.option.OptionParserResult;
import de.arthurpicht.meta.Const;
import de.arthurpicht.meta.cli.definitions.GlobalOptionsDef;
import de.arthurpicht.meta.cli.persistence.user.MetaDirFile;
import de.arthurpicht.meta.exception.MetaRuntimeException;
import de.arthurpicht.utils.io.nio2.FileUtils;

import java.io.IOException;
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
        MetaDirFile metaDirFile = new MetaDirFile();
        if (optionParserResultGlobal.hasOption(GlobalOptionsDef.META_DIR)) {
            metaDirString = optionParserResultGlobal.getValue(GlobalOptionsDef.META_DIR);
            metaDirSpecifier = MetaDirSpecifier.CLI;
        } else if (metaDirFile.exists()) {
            metaDirString = readMetaDirFromFile();
            metaDirSpecifier = MetaDirSpecifier.META_DIR_FILE;
        } else {
            metaDirString = FileUtils.getWorkingDir().toString();
            metaDirSpecifier = MetaDirSpecifier.WORKING_DIR;
        }

        Path metaDir = Paths.get(metaDirString).normalize();
        return new MetaDir(metaDir, metaDirSpecifier);
    }

    private static void assertExistenceOfMetaDir(MetaDir metaDir) {
        if (!Files.exists(metaDir.asPath()) || !Files.isDirectory(metaDir.asPath()))
            throw new MetaRuntimeException("Meta directory as specified by " + metaDir.getSpecifier().getText()
                    + " not found: [" + metaDir.asPath() + "].");
    }

    private static void assertContainsMetaConf(MetaDir metaDir) {
        Path metaConf = metaDir.asPath().resolve(Const.META_CONF__FILE_NAME);
        if (!Files.exists(metaConf) || !Files.isRegularFile(metaConf))
            throw new MetaRuntimeException("No " + Const.META_CONF__FILE_NAME + " [" + metaConf.toAbsolutePath() + "] found.");
    }

    private static String readMetaDirFromFile() {
        MetaDirFile metaDirFile = new MetaDirFile();
        try {
            return metaDirFile.read();
        } catch (IOException e) {
            throw new MetaRuntimeException("File [" + metaDirFile.asPath() + "] not found. Cause: " + e.getMessage());
        }
    }

}
