package de.arthurpicht.meta.helper;

import de.arthurpicht.meta.git.GitRepoUrl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GitRepoUrlTest {

    @Test
    public void testSsh() {
        String url = "git@bitbucket.org:mentalizr/m7r-backend-proc.git";
        GitRepoUrl gitRepoUrl = new GitRepoUrl(url);

        assertEquals("m7r-backend-proc", gitRepoUrl.getRepoName());
    }

    @Test
    public void testHttps() {
        String url = "https://github.com/arthurpicht/testRepo5.git";
        GitRepoUrl gitRepoUrl = new GitRepoUrl(url);
        assertEquals("testRepo5", gitRepoUrl.getRepoName());
    }

    @Test
    public void test() {
        String url = "git@github.com:arthurpicht/testRepo5.git";
        String urlReadOnly = "https://github.com/arthurpicht/testRepo5.git";
        GitRepoUrl gitRepoUrl = new GitRepoUrl(url, urlReadOnly);

        assertTrue(gitRepoUrl.hasUrlReadOnly());
        assertEquals(url, gitRepoUrl.getUrl());
        assertEquals(urlReadOnly, gitRepoUrl.getUrlReadOnly());
    }
}