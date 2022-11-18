package de.arthurpicht.meta.config;

import de.arthurpicht.meta.config.exceptions.ConfigurationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GitRepoUrlTest {

    @Test
    public void testSsh() throws ConfigurationException {
        String url = "git@bitbucket.org:mentalizr/m7r-backend-proc.git";
        GitRepoUrl gitRepoUrl = new GitRepoUrl(url);

        assertEquals("m7r-backend-proc", gitRepoUrl.getRepoName());
    }

    @Test
    public void testHttps() throws ConfigurationException {
        String url = "https://github.com/arthurpicht/testRepo5.git";
        GitRepoUrl gitRepoUrl = new GitRepoUrl(url);
        assertEquals("testRepo5", gitRepoUrl.getRepoName());
    }

    @Test
    public void test() throws ConfigurationException {
        String url = "git@github.com:arthurpicht/testRepo5.git";
        String urlReadOnly = "https://github.com/arthurpicht/testRepo5.git";
        GitRepoUrl gitRepoUrl = new GitRepoUrl(url, urlReadOnly);

        assertTrue(gitRepoUrl.hasUrlReadOnly());
        assertEquals(url, gitRepoUrl.getUrl());
        assertEquals(urlReadOnly, gitRepoUrl.getUrlReadOnly());
    }
}