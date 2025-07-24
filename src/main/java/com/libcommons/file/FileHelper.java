package com.libcommons.file;

import com.libcommons.classes.ServiceException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Component
@NoArgsConstructor
public class FileHelper {

    private String tempDirName = "temp";

    public FileHelper(String tempDirName) {
        this.tempDirName = tempDirName;
    }

    public Path getOrCreateTempDirectory() {
        Path tempDir = Paths.get(tempDirName);
        if (Files.notExists(tempDir)) {
            try {
                Files.createDirectories(tempDir);
            } catch (IOException e) {
                throw new ServiceException("Erro ao criar diretório temporário: " + tempDir, "FileHelper", "getOrCreateTempDirectory", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return tempDir;
    }

    public Path createTempFile(String prefix, String extension) throws IOException {
        Path tempDir = getOrCreateTempDirectory();
        return Files.createTempFile(tempDir, sanitizeFileName(prefix) + "_", "." + extension);
    }

    public Path saveStringToFile(String prefix, String extension, String content) throws IOException {
        Path tempFile = createTempFile(prefix, extension);
        Files.writeString(tempFile, content, StandardOpenOption.TRUNCATE_EXISTING);
        return tempFile;
    }

    public Path saveMultipartFileToTemp(MultipartFile file, String prefix) throws IOException {
        String extension = getFileExtension(file.getOriginalFilename());
        Path tempFile = createTempFile(prefix, extension);
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        return tempFile;
    }

    public void writeContentToFile(Path filePath, String content) throws IOException {
        Files.writeString(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public boolean deleteFile(Path filePath) {
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }
}
