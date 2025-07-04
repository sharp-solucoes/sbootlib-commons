package com.libcommons.file;

import com.libcommons.classes.ServiceException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
@NoArgsConstructor
public class FileHelper {

    private final String DEFAULT_TEMP_DIR = "temp";

    public Path getOrCreateTempDirectory() {
        Path tempDir = get(DEFAULT_TEMP_DIR);

        if (notExists(tempDir)) {
            createDirectory(tempDir);
        }

        return tempDir;
    }

    public Path get(String first) {
        return Paths.get(first);
    }

    public void writeContentToFile(File file, String content) throws IOException {
        Files.write(file.toPath(), content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    public File createTempFileWithContent(String prefix, String extension, String content) throws IOException {
        File file = createTempFile(prefix, extension);
        writeContentToFile(file, content);
        return file;
    }

    public boolean deleteFile(File file) {
        return file.delete();
    }

    private Path createDirectory(Path dir) {
        try {
            return Files.createDirectory(dir);
        } catch (IOException e) {
            throw new ServiceException("Erro ao criar diretório", "?", "?", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean notExists(Path path) {
        return Files.notExists(path);
    }

    public File createTempFile(String prefix, String extension) throws IOException {
        Path tempDir = getOrCreateTempDirectory();
        String sanitized = sanitizeFileName(prefix);
        String filename = sanitized + "." + extension;
        Path filePath = tempDir.resolve(filename);
        Files.createFile(filePath);
        return filePath.toFile();
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }
}