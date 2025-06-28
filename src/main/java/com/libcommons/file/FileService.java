package com.libcommons.file;

import com.libcommons.classes.ServiceException;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileService {

    private static final String DEFAULT_TEMP_DIR = "temp";

    public Path getOrCreateTempDirectory() {
        Path tempDir = this.get(DEFAULT_TEMP_DIR);

        if (this.notExists(tempDir)) {
            this.createDirectory(tempDir);
        }

        return tempDir;
    }

    public Path get(String first) {
        return Paths.get(first);
    }

    public static void writeContentToFile(File file, String content) throws IOException {
        Files.write(file.toPath(), content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    public File createTempFileWithContent(String prefix, String extension, String content) throws IOException {
        File file = this.createTempFile(prefix, extension);
        writeContentToFile(file, content);
        return file;
    }

    public static boolean deleteFile(File file) {
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

    private File createTempFile(String prefix, String extension) throws IOException {

        Path tempDir = this.getOrCreateTempDirectory();

        String sanitized = sanitizeFileName(prefix);
        String filename = sanitized + "." + extension;

        Path filePath = tempDir.resolve(filename);
        Files.createFile(filePath);
        return filePath.toFile();
    }

    private static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }
}
