package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.configuration.ConfigurationFactory;
import de.arthurpicht.configuration.ConfigurationFileNotFoundException;
import de.arthurpicht.meta.cli.target.RedundantTargetException;
import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.cli.target.UnknownTargetException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class RepoConfigTest {

    private static GeneralConfig generalConfig;

    @BeforeAll
    public static void prepare() throws IOException, ConfigurationFileNotFoundException, ConfigurationException {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        configurationFactory.addConfigurationFileFromFilesystem(new File("src/test/resources/meta1/meta.conf"));

        generalConfig = new GeneralConfig(
                configurationFactory.getConfiguration("general"),
                Paths.get("src/test/resources/meta1/"));
    }

    private Configuration getSection(String section) throws IOException, ConfigurationFileNotFoundException {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        configurationFactory.addConfigurationFileFromFilesystem(new File("src/test/resources/meta1/meta.conf"));
        assertTrue(configurationFactory.hasSection(section));
        return configurationFactory.getConfiguration(section);
    }

    @Test
    void testSimple() throws IOException, ConfigurationFileNotFoundException, ConfigurationException, RedundantTargetException, UnknownTargetException {
        Configuration testRepo1 = getSection("simple");
        RepoConfig repoConfig = RepoConfigFactory.create(testRepo1, generalConfig);

        assertEquals("simple", repoConfig.getRepoId());
        assertEquals("git@github.com:arthurpicht/testRepoSimple.git", repoConfig.getGitRepoUrl());
        assertFalse(repoConfig.hasGitRepoUrlReadOnly());
        assertEquals(Paths.get("src/test/resources").toAbsolutePath().toString(), repoConfig.getDestinationPath().toString());
        assertFalse(repoConfig.hasAlteredRepoName());
        assertEquals("testRepoSimple", repoConfig.getRepoName());
        assertFalse(repoConfig.hasAlteredBranch());
        assertTrue(repoConfig.hasTarget(Target.DEV));
        assertTrue(repoConfig.hasTarget(Target.PROD));
    }

    @Test
    void testDestinationDirRelative() throws IOException, ConfigurationFileNotFoundException, ConfigurationException, RedundantTargetException, UnknownTargetException {
        Configuration testRepo1 = getSection("testRepo1");
        RepoConfig repoConfig = RepoConfigFactory.create(testRepo1, generalConfig);

        assertEquals("testRepo1", repoConfig.getRepoId());
        assertEquals("git@github.com:arthurpicht/testRepo1.git", repoConfig.getGitRepoUrl());
        assertEquals(Paths.get("src/test/resources/core").toAbsolutePath().toString(), repoConfig.getDestinationPath().toString());
        assertTrue(repoConfig.getDestinationPath().isAbsolute());
        assertFalse(repoConfig.hasAlteredRepoName());
        assertEquals("testRepo1", repoConfig.getRepoName());
        assertFalse(repoConfig.hasAlteredBranch());
    }

    @Test
    void testDestinationDirAbsolute() throws IOException, ConfigurationFileNotFoundException, ConfigurationException, RedundantTargetException, UnknownTargetException {
        Configuration testRepo1 = getSection("testRepo2");
        RepoConfig repoConfig = RepoConfigFactory.create(testRepo1, generalConfig);

        assertEquals("testRepo2", repoConfig.getRepoId());
        assertEquals("git@github.com:arthurpicht/testRepo2.git", repoConfig.getGitRepoUrl());
        assertEquals("/my/absolute/path", repoConfig.getDestinationPath().toString());
        assertTrue(repoConfig.getDestinationPath().isAbsolute());
        assertFalse(repoConfig.hasAlteredRepoName());
        assertEquals("testRepo2", repoConfig.getRepoName());
        assertFalse(repoConfig.hasAlteredBranch());
    }

    @Test
    void testDestinationAlteredBranch() throws IOException, ConfigurationFileNotFoundException, ConfigurationException, RedundantTargetException, UnknownTargetException {
        Configuration testRepo1 = getSection("testRepo3");
        RepoConfig repoConfig = RepoConfigFactory.create(testRepo1, generalConfig);

        assertTrue(repoConfig.hasAlteredBranch());
        assertEquals("develop", repoConfig.getBranch());
    }

    @Test
    void testDestinationAlteredRepoName() throws IOException, ConfigurationFileNotFoundException, ConfigurationException, RedundantTargetException, UnknownTargetException {
        Configuration testRepo1 = getSection("testRepo4");
        RepoConfig repoConfig = RepoConfigFactory.create(testRepo1, generalConfig);

        assertTrue(repoConfig.hasAlteredRepoName());
        assertEquals("myCustomName", repoConfig.getRepoName());
    }

    @Test
    void urlMissing_neg() throws IOException, ConfigurationFileNotFoundException, RedundantTargetException, UnknownTargetException {
        Configuration testRepo1 = getSection("testRepo5");
        try {
            RepoConfigFactory.create(testRepo1, generalConfig);
            fail(ConfigurationException.class.getSimpleName() + " expected.");
        } catch (ConfigurationException e) {
            // din
        }
    }

    @Test
    void testUrlReadOnly() throws IOException, ConfigurationFileNotFoundException, ConfigurationException, RedundantTargetException, UnknownTargetException {
        Configuration testRepo1 = getSection("testRepo6");

        RepoConfig repoConfig = RepoConfigFactory.create(testRepo1, generalConfig);

        assertEquals("git@github.com:arthurpicht/testRepo6.git", repoConfig.getGitRepoUrl());
        assertTrue(repoConfig.hasGitRepoUrlReadOnly());
        assertEquals("https://github.com/arthurpicht/testRepo6.git", repoConfig.getGitRepoUrlReadOnly());
    }

    @Test
    void testTargetDev() throws IOException, ConfigurationFileNotFoundException, ConfigurationException, RedundantTargetException, UnknownTargetException {
        Configuration testRepo1 = getSection("testRepo7");

        RepoConfig repoConfig = RepoConfigFactory.create(testRepo1, generalConfig);

        assertTrue(repoConfig.hasTarget(Target.DEV));
        assertFalse(repoConfig.hasTarget(Target.PROD));
    }

    @Test
    void testTargetProd() throws IOException, ConfigurationFileNotFoundException, ConfigurationException, RedundantTargetException, UnknownTargetException {
        Configuration testRepo1 = getSection("testRepo8");

        RepoConfig repoConfig = RepoConfigFactory.create(testRepo1, generalConfig);

        assertFalse(repoConfig.hasTarget(Target.DEV));
        assertTrue(repoConfig.hasTarget(Target.PROD));
    }

    @Test
    void unknownTarget_neg() throws IOException, ConfigurationFileNotFoundException {
        Configuration testRepo1 = getSection("testRepo9");

        UnknownTargetException e = Assertions.assertThrows(UnknownTargetException.class, () -> RepoConfigFactory.create(testRepo1, generalConfig));
        assertEquals(e.getMessage(), "Unknown target: [rubbish] in project [testRepo9].");
    }

    @Test
    void redundantTarget_neg() throws IOException, ConfigurationFileNotFoundException {
        Configuration testRepo1 = getSection("testRepo10");

        RedundantTargetException e = Assertions.assertThrows(RedundantTargetException.class, ()
                -> RepoConfigFactory.create(testRepo1, generalConfig));
        assertEquals(e.getMessage(), "Redundant target: [dev] in project [testRepo10].");
    }

}
