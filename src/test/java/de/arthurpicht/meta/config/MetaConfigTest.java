package de.arthurpicht.meta.config;

import de.arthurpicht.meta.cli.target.Target;
import de.arthurpicht.meta.cli.target.Targets;
import de.arthurpicht.meta.config.exceptions.ConfigurationException;
import de.arthurpicht.utils.core.collection.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetaConfigTest {

    private static MetaConfig metaConfig3;

    @BeforeAll
    public static void prepare() throws ConfigurationException {
        metaConfig3 = MetaConfigFactory.create(Paths.get("src/test/resources/meta3"));
    }

    @Test
    public void getRepoNamesMeta1() {
        assertEquals(8, metaConfig3.getRepoNames().size());
        assertEquals(
                Lists.newArrayList("simple", "testRepo1", "testRepo2", "testRepo3", "testRepo4", "testRepo5", "testRepo6", "testRepo7"),
                metaConfig3.getRepoNames()
        );
    }

    @Test
    public void getRepoConfig() {
        RepoConfig repoConfig = metaConfig3.getRepoConfig("simple");
        assertEquals("simple", repoConfig.getRepoId());
    }

    @Test
    public void getRepoConfigsForTarget() {
        Target devTarget = new Target("dev");
        List<RepoConfig> repoConfigList = metaConfig3.getRepoConfigsForTarget(devTarget);

        List<String> actualRepoIdList = repoConfigList.stream().map(RepoConfig::getRepoId).collect(Collectors.toUnmodifiableList());
        List<String> expectedRepoIdList = Lists.newArrayList("simple", "testRepo1", "testRepo2", "testRepo3", "testRepo4", "testRepo5", "testRepo6");

        assertEquals(expectedRepoIdList, actualRepoIdList);
    }

    @Test
    public void defaultGeneralSection() throws ConfigurationException {
        Path metaDir = Paths.get("src/test/resources/noGeneral");
        MetaConfig metaConfig = MetaConfigFactory.create(metaDir);
        GeneralConfig generalConfig = metaConfig.getGeneralConfig();

        assertEquals(generalConfig.getReferencePath(), metaDir.getParent().toAbsolutePath());

        Targets targets = generalConfig.getTargets();

        assertTrue(targets.hasTarget(Target.DEV));
        assertTrue(targets.hasTarget(Target.PROD));
        assertEquals(2, targets.getAllTargetNames().size());
    }

}
