package de.arthurpicht.meta.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesHelper {

    public static boolean isDirectoryNonEmpty(Path dir) throws IOException {
        if (!Files.exists(dir)) return false;
        if (!Files.isDirectory(dir)) return false;
        return Files.list(dir).findAny().isPresent();
    }

}
