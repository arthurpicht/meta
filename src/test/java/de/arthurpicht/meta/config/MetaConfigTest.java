package de.arthurpicht.meta.config;

import de.arthurpicht.meta.config.exceptions.ConfigurationException;
import de.arthurpicht.utils.core.collection.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MetaConfigTest {

    private static MetaConfig metaConfig3;

    @BeforeAll
    public static void prepare() throws ConfigurationException {
        metaConfig3 = MetaConfigFactory.create(Paths.get("src/test/resources/meta3"));
    }

    @Test
    public void projectNamesMeta1() {
        assertEquals(8, metaConfig3.getProjectNames().size());
        assertEquals(Lists.newArrayList("simple", "testRepo1", "testRepo2", "testRepo3", "testRepo4", "testRepo5", "testRepo6", "testRepo7"), metaConfig3.getProjectNames());
    }

    @Test
    public void getProjectConfig() {
        RepoConfig repoConfig = metaConfig3.getProjectConfig("simple");
        assertEquals("simple", repoConfig.getRepoId());
    }

}
