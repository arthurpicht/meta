package de.arthurpicht.meta.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GitRepoUrlTest {

    @Test
    public void test() {
        String url = "git@bitbucket.org:mentalizr/m7r-backend-proc.git";
        GitRepoUrl gitRepoUrl = new GitRepoUrl(url);

        assertEquals("m7r-backend-proc", gitRepoUrl.getRepoName());
    }

}