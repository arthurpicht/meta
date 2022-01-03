package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.cli.target.Targets;
import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneralConfigTest {

    private Configuration getSectionGeneral(Path metaDir) throws IOException, ConfigurationFileNotFoundException {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        configurationFactory.addConfigurationFileFromFilesystem(metaDir.resolve("meta.conf").toFile());
        assertTrue(configurationFactory.hasSection(GeneralConfig.SECTION_GENERAL));
        return configurationFactory.getConfiguration(GeneralConfig.SECTION_GENERAL);
    }

    @Test
    void getReferencePath() throws IOException, ConfigurationFileNotFoundException, ConfigurationException {
        Path metaDir = Paths.get("src/test/resources/meta1");
        Configuration generalConfiguration = getSectionGeneral(metaDir);

        GeneralConfig generalConfig = new GeneralConfig(generalConfiguration, metaDir);
        Path referencePath = generalConfig.getReferencePath();

        Path expectedPath = Paths.get("src/test/resources").toAbsolutePath();
        assertEquals(expectedPath.toString(), referencePath.toString());
    }

    @Test
    void defaultTargets() throws IOException, ConfigurationFileNotFoundException, ConfigurationException {
        Path metaDir = Paths.get("src/test/resources/meta1");
        Configuration generalConfiguration = getSectionGeneral(metaDir);

        GeneralConfig generalConfig = new GeneralConfig(generalConfiguration, metaDir);
        Targets targets = generalConfig.getTargets();

        assertTrue(targets.hasTarget(Target.DEV));
        assertTrue(targets.hasTarget(Target.PROD));
        assertEquals(2, targets.getAllTargetNames().size());
    }

    @Test
    void specifiedTargets() throws IOException, ConfigurationFileNotFoundException, ConfigurationException {
        Path metaDir = Paths.get("src/test/resources/meta2");
        Configuration generalConfiguration = getSectionGeneral(metaDir);

        GeneralConfig generalConfig = new GeneralConfig(generalConfiguration, metaDir);
        Targets targets = generalConfig.getTargets();

        System.out.println(Strings.listing(targets.getAllTargetNames(), ", "));

        assertTrue(targets.hasTarget("dev"));
        assertTrue(targets.hasTarget("prod-a"));
        assertTrue(targets.hasTarget("prod-b"));

        assertEquals(3, targets.getAllTargetNames().size());
    }

}