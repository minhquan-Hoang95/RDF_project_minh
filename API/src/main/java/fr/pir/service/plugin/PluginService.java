package fr.pir.service.plugin;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PluginService {

    private static final Logger L = LogManager.getLogger(PluginService.class);

    public File zipFolder() throws IOException {
        L.debug("");

        Path sourceFolderPath = Paths.get("plugin");
        Path zipPath = Files.createTempFile("plugin-", ".zip");

        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Files.walk(sourceFolderPath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceFolderPath.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }

        return zipPath.toFile();
    }

}
