package de.arthurpicht.meta.helper;

import de.arthurpicht.utils.io.nio2.FileUtils;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilesHelperTest {

    @Test
    void isRootDirectory() {
        Path path = Paths.get("/");
        assertTrue(FileUtils.isRootDirectory(path));
    }

    @Test
    void isRootDirectoryNeb() {
        Path path = Paths.get("src");
        assertFalse(FileUtils.isRootDirectory(path));
    }

}