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

class RepoConfigTest {

    private Configuration getSection(String section) throws IOException, ConfigurationFileNotFoundException {
        ConfigurationFactory configurationFactory = new ConfigurationFactory();
        configurationFactory.addConfigurationFileFromFilesystem(new File("src/test/resources/meta/meta1.conf"));
        assertTrue(configurationFactory.hasSection(section));
        return configurationFactory.getConfiguration(section);
    }

    @Test
    void testSimple() throws IOException, ConfigurationFileNotFoundException, ConfigurationException {
        Configuration testRepo1 = getSection("simple");
        Path referencePath = Paths.get("src/test/resources");

        RepoConfig repoConfig = new RepoConfig(testRepo1, referencePath);

        assertEquals("simple", repoConfig.getRepoId());
        assertEquals("git@github.com:arthurpicht/testRepoSimple.git", repoConfig.getGitRepoUrl());
        assertFalse(repoConfig.hasGitRepoUrlReadOnly());
        assertEquals("src/test/resources", repoConfig.getDestinationPath().toString());
        assertFalse(repoConfig.hasAlteredRepoName());
        assertEquals("testRepoSimple", repoConfig.getRepoName());
        assertFalse(repoConfig.hasAlteredBranch());
    }

    @Test
    void testDestinationDirRelative() throws IOException, ConfigurationFileNotFoundException, ConfigurationException {
        Configuration testRepo1 = getSection("testRepo1");
        Path referencePath = Paths.get("src/test/resources");

        RepoConfig repoConfig = new RepoConfig(testRepo1, referencePath);

        assertEquals("testRepo1", repoConfig.getRepoId());
        assertEquals("git@github.com:arthurpicht/testRepo1.git", repoConfig.getGitRepoUrl());
        assertEquals("src/test/resources/core", repoConfig.getDestinationPath().toString());
        assertFalse(repoConfig.getDestinationPath().isAbsolute());
        assertFalse(repoConfig.hasAlteredRepoName());
        assertEquals("testRepo1", repoConfig.getRepoName());
        assertFalse(repoConfig.hasAlteredBranch());
    }

    @Test
    void testDestinationDirAbsolute() throws IOException, ConfigurationFileNotFoundException, ConfigurationException {
        Configuration testRepo1 = getSection("testRepo2");
        Path referencePath = Paths.get("src/test/resources");

        RepoConfig repoConfig = new RepoConfig(testRepo1, referencePath);

        assertEquals("testRepo2", repoConfig.getRepoId());
        assertEquals("git@github.com:arthurpicht/testRepo2.git", repoConfig.getGitRepoUrl());
        assertEquals("/my/absolute/path", repoConfig.getDestinationPath().toString());
        assertTrue(repoConfig.getDestinationPath().isAbsolute());
        assertFalse(repoConfig.hasAlteredRepoName());
        assertEquals("testRepo2", repoConfig.getRepoName());
        assertFalse(repoConfig.hasAlteredBranch());
    }

    @Test
    void testDestinationAlteredBranch() throws IOException, ConfigurationFileNotFoundException, ConfigurationException {
        Configuration testRepo1 = getSection("testRepo3");
        Path referencePath = Paths.get("src/test/resources");

        RepoConfig repoConfig = new RepoConfig(testRepo1, referencePath);

        assertTrue(repoConfig.hasAlteredBranch());
        assertEquals("develop", repoConfig.getBranch());
    }

    @Test
    void testDestinationAlteredRepoName() throws IOException, ConfigurationFileNotFoundException, ConfigurationException {
        Configuration testRepo1 = getSection("testRepo4");
        Path referencePath = Paths.get("src/test/resources");

        RepoConfig repoConfig = new RepoConfig(testRepo1, referencePath);

        assertTrue(repoConfig.hasAlteredRepoName());
        assertEquals("myCustomName", repoConfig.getRepoName());
    }

    @Test
    void urlMissing_neg() throws IOException, ConfigurationFileNotFoundException {
        Configuration testRepo1 = getSection("testRepo5");
        Path referencePath = Paths.get("src/test/resources");

        try {
            new RepoConfig(testRepo1, referencePath);
            fail(ConfigurationException.class.getSimpleName() + " expected.");
        } catch (ConfigurationException e) {
            // din
        }
    }

    @Test
    void testUrlReadOnly() throws IOException, ConfigurationFileNotFoundException, ConfigurationException {
        Configuration testRepo1 = getSection("testRepo6");
        Path referencePath = Paths.get("src/test/resources");

        RepoConfig repoConfig = new RepoConfig(testRepo1, referencePath);

        assertEquals("git@github.com:arthurpicht/testRepo6.git", repoConfig.getGitRepoUrl());
        assertTrue(repoConfig.hasGitRepoUrlReadOnly());
        assertEquals("https://github.com/arthurpicht/testRepo6.git", repoConfig.getGitRepoUrlReadOnly());
    }

}