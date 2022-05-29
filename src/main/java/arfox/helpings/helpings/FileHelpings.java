package arfox.helpings.helpings;

import arfox.helpings.lambda.ThrowingPass;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelpings {
    public static Path createFolder(Path path) {
        return ThrowingPass.of(() -> Files.createDirectories(path));
    }

    public static String getFilename(String path) {return Path.of(path).getFileName().toString();}
}
