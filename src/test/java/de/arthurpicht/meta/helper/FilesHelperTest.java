package de.arthurpicht.meta.helper;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FilesHelperTest {

    @Test
    void isRootDirectory() {
        Path path = Paths.get("/");
        assertTrue(FilesHelper.isRootDirectory(path));
    }

    @Test
    void isRootDirectoryNeb() {
        Path path = Paths.get("src");
        assertFalse(FilesHelper.isRootDirectory(path));
    }

}