package de.arthurpicht.meta.tasks.feature;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureBranchNameTest {

    @Test
    void createByBranchName() {
        FeatureBranchName featureBranchName = FeatureBranchName.createByBranchName("feature-test");
        assertEquals("feature-test", featureBranchName.getBranchName());
        assertEquals("test", featureBranchName.getFeatureName());
    }

    @Test
    void createByBranchName_getNoFeature() {
        assertThrows(IllegalArgumentException.class, () -> FeatureBranchName.createByBranchName("dummy-test"));
    }

    @Test
    void createByBranchName_getNoFeatureName() {
        assertThrows(IllegalArgumentException.class, () -> FeatureBranchName.createByBranchName("feature-"));
    }

    @Test
    void createByFeatureName() {
        FeatureBranchName featureBranchName = FeatureBranchName.createByFeatureName("test");
        assertEquals("feature-test", featureBranchName.getBranchName());
        assertEquals("test", featureBranchName.getFeatureName());
    }

    @Test
    void isFeatureBranchName() {
        assertTrue(FeatureBranchName.isFeatureBranchName("feature-test"));
    }

    @Test
    void isFeatureBranchNameNeg() {
        assertFalse(FeatureBranchName.isFeatureBranchName("test"));
    }

    @Test
    void isFeatureBranchNameNeg2() {
        assertFalse(FeatureBranchName.isFeatureBranchName("feature-"));
    }



}