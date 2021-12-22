package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class GeneralConfigTest {

    private Configuration getSectionGeneral() throws IOException, ConfigurationFileNotFoundException {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        configurationFactory.addConfigurationFileFromFilesystem(new File("src/test/resources/meta1/meta.conf"));
        assertTrue(configurationFactory.hasSection(GeneralConfig.SECTION_GENERAL));
        return configurationFactory.getConfiguration(GeneralConfig.SECTION_GENERAL);
    }

    @Test
    void getReferencePath() throws IOException, ConfigurationFileNotFoundException, ConfigurationException {
        System.out.println(System.getProperty("user.dir"));

        Configuration generalConfiguration = getSectionGeneral();
        Path metaPath = Paths.get("src/test/resources/meta1");

        GeneralConfig generalConfig = new GeneralConfig(generalConfiguration, metaPath);
        Path referencePath = generalConfig.getReferencePath();

        Path expectedPath = Paths.get(System.getProperty("user.dir")).resolve("src/test/resources");
        assertEquals(expectedPath.toString(), referencePath.toString());
    }

}